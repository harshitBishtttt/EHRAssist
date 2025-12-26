package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientPrescriptionResponse;
import EHRAssist.dto.response.patientConditionResponse.Meta;
import EHRAssist.dto.response.patientPrescriptionResponse.*;
import EHRAssist.dto.response.patientSearchResponse.EntryMeta;
import EHRAssist.dto.response.patientSearchResponse.Link;
import EHRAssist.dto.response.personProcedureResponse.Search;
import EHRAssist.model.PersonPrescription;
import EHRAssist.repository.PatientPrescriptionRepository;
import EHRAssist.service.PatientPrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PatientPrescriptionServiceImpl implements PatientPrescriptionService {
    @Autowired
    private PatientPrescriptionRepository patientPrescriptionRepository;


    private Resource getResourceType(PersonPrescription obj) {
        Resource resource = new Resource();
        resource.setId(obj.getRowId());
        resource.setResourceType("MedicationRequest");
        resource.setMedicationCodeableConcept(MedicationCodeableConcept.builder()
                .text(obj.getDrugNameGeneric()).coding(List.of(Coding.builder()
                        .code(obj.getFormularyDrugCd())
                        .system("http://www.nlm.nih.gov/research/umls/rxnorm")
                        .display(obj.getDrug()).build())).build());
        resource.setStatus(obj.getEndDate().isBefore(LocalDateTime.now())
                ? "completed"
                : "pending");
        resource.setAuthoredOn(obj.getStartDate().toString());
        resource.setDosageInstruction(DosageInstruction.builder()
                .route(Route.builder()
                        .coding(List.of(Coding.builder()
                                .code(obj.getRoute())
                                .system("http://www.nlm.nih.gov/research/umls/rxnorm")
                                .display("Oral").build())).build())
                .text(obj.getProdStrength())
                .timing(Timing.builder()
                        .repeat(Repeat.builder()
                                .frequency("2")
                                .period("1")
                                .periodUnit("d").build()).build())
                .doseAndRate(List.of(DoseAndRate.builder()
                        .doseQuantity(DoseQuantity.builder()
                                .value(obj.getDoseValRx())
                                .unit(obj.getDoseUnitRx())
                                .code("{" + obj.getFormularyDrugCd() + "}")
                                .system("http://www.nlm.nih.gov/research/umls/rxnorm").build())
                        .build()))
                .build());
        resource.setMeta(EntryMeta.builder().versionId("1")
                .source("#ULtyC0aiSJxdl9E2")
                .lastUpdated("2025-12-22T09:53:10.308+00:00").build());
        resource.setIntent("order");
        resource.setPriority("routine");
        resource.setSubject(Subject.builder().display("").reference("Patient/" + obj.getSubjectId()).build());
        resource.setReasonCode(List.of(ReasonCode.builder().text("Fever and body pain").build()));
        resource.setNote(List.of(Note.builder().text("Do not exceed recommended dose").build()));

        return resource;
    }

    public PatientPrescriptionResponse getPatientPrescription(Integer subjectId, Integer prescriptionId) {
        if (subjectId == null && prescriptionId == null) {
            return null;
        }
        PatientPrescriptionResponse response = new PatientPrescriptionResponse();
        List<PersonPrescription> bySubjectIdOrRowId =
                patientPrescriptionRepository.findBySubjectIdOrRowId(subjectId, prescriptionId);
        if (!ObjectUtils.isEmpty(bySubjectIdOrRowId)) {
            response.setId(subjectId.toString());
            response.setTotal(bySubjectIdOrRowId.size());
            response.setLink(Link.builder().build());
            response.setMeta(null);
            List<Entry> entryList = bySubjectIdOrRowId.stream().map(ittr -> {
                return Entry.builder().fullUrl("10.131.58.59:481/baseR4/MedicationRequest/" + subjectId)
                        .resource(getResourceType(ittr))
                        .search(Search.builder().mode("matched").build()).build();
            }).toList();
            response.setResourceType("Bundle");
            response.setType("searchset");
            response.setEntry(entryList);
            response.setMeta(Meta.builder().lastUpdated("2025-12-26T05:53:23.339+00:00").build());
            response.setLink(Link.builder().relation("self").url("https://hapi.fhir.org/baseR4/MedicationRequest?subject=" + subjectId).build());
        }
        return response;
    }
}
