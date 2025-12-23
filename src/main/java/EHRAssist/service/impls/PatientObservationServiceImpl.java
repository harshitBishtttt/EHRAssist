package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientObservationResponse;
import EHRAssist.dto.response.patientObservationResponse.*;
import EHRAssist.dto.response.personProcedureResponse.Search;
import EHRAssist.repository.PersonMeasurementRepository;
import EHRAssist.service.PatientObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class PatientObservationServiceImpl implements PatientObservationService {
    @Autowired
    private PersonMeasurementRepository personMeasurementRepository;

    Resource resourceMapper(Object[] ittr) {
        Resource resource = new Resource();
        resource.setResourceType("Observations");
        resource.setId((Integer) ittr[0]);
        Component c = new Component();
        Code coding = Code.builder().coding(List.of(Coding.builder()
                .code(!ObjectUtils.isEmpty(ittr[12]) ? (String) ittr[12] : "")
                .display("http://loinc.org")
                .system(!ObjectUtils.isEmpty(ittr[9]) ? (String) ittr[9] : "").build())).build();
        c.setCode(coding);
        c.setValueQuantity(ValueQuantity.builder()
                .value(!ObjectUtils.isEmpty(ittr[6]) ? (double) ittr[6] : 0.0)
                .unit((String) ittr[7]).code("/" + (String) ittr[7]).system("http://unitsofmeasure.org").build());
        resource.setComponent(List.of(c));
        resource.setCategory(Category.builder()
                .coding(List.of(Coding.builder()
                        .system("")
                        .code((String) ittr[10])
                        .display((String) ittr[9]).build())).build());
        resource.setStatus("Final");
        return resource;
    }

    public PatientObservationResponse getPatientObservations(Integer subject,
                                                             String code,
                                                             Integer encounter,
                                                             Pageable pageable) {

        PatientObservationResponse response = new PatientObservationResponse();
        List<Object[]> latestMeasurements = personMeasurementRepository
                .findLatestMeasurements(subject, code, encounter);
        if (!latestMeasurements.isEmpty()) {
            response.setEntry(latestMeasurements.stream().map(ittr -> {
                return Entry.builder().resource(resourceMapper(ittr))
                        .fullUrl("").search(Search.builder()
                                .mode("matched").build()).build();
            }).toList());
            response.setTotal(latestMeasurements.size());
            response.setType("Bundle");
            response.setId(subject.toString());
            response.setResourceType("Bundle");
        }
        return response;
    }
}
