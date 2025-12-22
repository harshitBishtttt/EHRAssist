package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientObservationResponse;
import EHRAssist.dto.response.patientObservationResponse.*;
import EHRAssist.dto.response.personProcedureResponse.Category;
import EHRAssist.dto.response.personProcedureResponse.Search;
import EHRAssist.repository.PersonMeasurementRepository;
import EHRAssist.service.PatientObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class PatientObservationServiceImpl implements PatientObservationService {
    @Autowired
    private PersonMeasurementRepository personMeasurementRepository;

    public PatientObservationResponse getPatientObservations(Integer subject,
                                                             String code,
                                                             Integer encounter,
                                                             Pageable pageable) {

        PatientObservationResponse response = new PatientObservationResponse();
        List<Object[]> latestMeasurements = personMeasurementRepository
                .findLatestMeasurements(subject, code, encounter);
        if (!latestMeasurements.isEmpty()) {
            Resource resource = new Resource();
            resource.setResourceType("Observations");
            Object[] row = latestMeasurements.getFirst();
            resource.setId((Integer) row[1]);
            resource.setMeta(null);
            List<Component> componentResponse = latestMeasurements.stream().map(ittr -> {
                Component c = new Component();
                Code coding = Code.builder().coding(List.of(Coding.builder()
                        .code(!ObjectUtils.isEmpty(ittr[12]) ? (String) ittr[12] : "")
                        .display("http://loinc.org")
                        .system(!ObjectUtils.isEmpty(ittr[9]) ? (String) ittr[9] : "").build())).build();
                c.setCode(coding);
                c.setValueQuantity(ValueQuantity.builder()
                        .value(!ObjectUtils.isEmpty(ittr[6]) ? (double) ittr[6] : 0.0)
                        .unit((String) ittr[7]).code("/" + (String) ittr[7]).system("http://unitsofmeasure.org").build());
                return c;
            }).toList();
            resource.setComponent(componentResponse);
            Category cat = new Category();
            cat.setCoding(new ArrayList<>(new HashSet(latestMeasurements.stream().map(ittr -> {
                return Coding.builder().system("").code((String) ittr[10]).display((String) ittr[9]).build();
            }).toList())));
            resource.setCategory(cat);
            resource.setStatus("Final");
            response.setEntry(List.of(Entry.builder().resource(resource)
                    .fullUrl("").search(Search.builder()
                            .mode("matched").build()).build()));
            response.setTotal(latestMeasurements.size());
            response.setType("Bundle");

        }
        return response;
    }
}
