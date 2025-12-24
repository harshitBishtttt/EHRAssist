package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientObservationResponse;
import EHRAssist.dto.response.patientConditionResponse.Link;
import EHRAssist.dto.response.patientObservationResponse.*;
import EHRAssist.dto.response.personProcedureResponse.Search;
import EHRAssist.repository.PersonMeasurementRepository;
import EHRAssist.service.PatientObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PatientObservationServiceImpl implements PatientObservationService {
    @Autowired
    private PersonMeasurementRepository personMeasurementRepository;

    static String toVitalSignsCode(String input) {
        return input == null
                ? null
                : input.trim()
                .toLowerCase()
                .replaceAll("\\s+", "-");
    }

    private Resource resourceMapper(Object[] ittr) {
        Resource resource = new Resource();
        resource.setResourceType("Observation");
        resource.setId(ittr[0].toString());
        resource.setMeta(EntryMeta.builder().versionId("1").lastUpdated(OffsetDateTime.now(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")))
                .profile(List.of("http://hl7.org/fhir/StructureDefinition/vitalsigns"))
                .source("#wnzvyWyVXFVJbcHX").build());
        resource.setText(Text.builder()
                .div("<div xmlns=\"http://www.w3.org/1999/xhtml\">" + ittr[9] + " observation (no performer)</div>")
                .status("generated").build());
        resource.setIdentifier(Identifier.builder().system("http://hospital.example.org/observations")
                .value("BP-2025-0002").build());
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
        String s = (String) ittr[10];
        resource.setCategory(Category.builder()
                .coding(List.of(Coding.builder()
                        .system("http://terminology.hl7.org/CodeSystem/observation-category")
                        .code(toVitalSignsCode((String) ittr[9]))
                        .display((String) ittr[9]).build())).text((String) ittr[9]).build());
        resource.setStatus("final");
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
                        .fullUrl("10.131.58.59:481/baseR4/Observation/" + subject).search(Search.builder()
                                .mode("matched").build()).build();
            }).toList());
            response.setTotal(latestMeasurements.size());
            response.setType("searchset");
            response.setId(subject.toString());
            response.setResourceType("Bundle");
            response.setMeta(Meta.builder().lastUpdated(OffsetDateTime.now(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))).build());
            response.setLink(Link.builder()
                    .url("10.131.58.59:481/baseR4/Observation?code=" + code + "&subject=" + subject)
                    .relation("self").build());

        }
        return response;
    }
}
