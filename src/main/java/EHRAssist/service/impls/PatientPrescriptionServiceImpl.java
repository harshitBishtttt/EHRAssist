package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientPrescriptionResponse;
import EHRAssist.dto.response.patientPrescriptionResponse.*;
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
                                .display(obj.getDrug()).build(),
                        Coding.builder()
                                .code(obj.getGsn().toString())
                                .display(obj.getDrugNameGeneric())
                                .system("").build(),
                        Coding.builder()
                                .code(obj.getNdc().toString())
                                .display(obj.getDrugNameGeneric())
                                .system("").build())).build());
        resource.setStatus(obj.getEndDate().isBefore(LocalDateTime.now())
                ? "completed"
                : "pending");
        resource.setAuthoredOn(obj.getStartDate().toString());
        resource.setDosageInstruction(DosageInstruction.builder()
                .route(Route.builder()
                        .coding(List.of(Coding.builder()
                                .code(obj.getRoute())
                                .system("")
                                .display("http://terminology.hl7.org/CodeSystem/route-codes").build())).build())
                .text(obj.getProdStrength()).doseAndRate(List.of(DoseAndRate.builder()
                        .doseQuantity(DoseQuantity.builder()
                                .value(obj.getDoseValRx())
                                .unit(obj.getDoseUnitRx())
                                .code(obj.getFormularyDrugCd())
                                .system("").build())
                        .build()))
                .build());
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
                return Entry.builder().fullUrl("").resource(getResourceType(ittr))
                        .search(Search.builder().mode("matched").build()).build();
            }).toList();
            response.setEntry(entryList);
        }
        return response;
    }
}
