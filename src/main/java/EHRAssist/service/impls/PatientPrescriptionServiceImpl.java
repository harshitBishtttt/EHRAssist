package EHRAssist.service.impls;

import EHRAssist.exceptionHandler.exceptions.FhirBadRequestException;
import EHRAssist.model.PersonName;
import EHRAssist.model.PersonPrescription;
import EHRAssist.repository.PatientPrescriptionRepository;
import EHRAssist.service.PatientPrescriptionService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PatientPrescriptionServiceImpl implements PatientPrescriptionService {
    @Autowired
    private PatientPrescriptionRepository patientPrescriptionRepository;

    private MedicationRequest mapToMedicationRequest(PersonPrescription obj) {
        MedicationRequest mr = new MedicationRequest();
        mr.setId(obj.getRowId().toString());
        mr.setMeta(new Meta()
                .setVersionId("1")
                .setSource("#ULtyC0aiSJxdl9E2")
                .setLastUpdated(Date.from(Instant.parse("2025-12-22T09:53:10.308Z"))));
        mr.setStatus(MedicationRequest.MedicationRequestStatus.ONHOLD);
        mr.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
        mr.setPriority(MedicationRequest.MedicationRequestPriority.ROUTINE);
        CodeableConcept med = new CodeableConcept();
        med.setText(obj.getDrug());
        med.addCoding(new Coding()
                .setSystem("http://www.nlm.nih.gov/research/umls/rxnorm")
                .setCode(obj.getFormularyDrugCd())
                .setDisplay(obj.getDrug()));
        mr.setMedication(med);
        Reference subjectRef = new Reference();
        subjectRef.setReference("Patient/" + obj.getSubjectId());
        if (!ObjectUtils.isEmpty(obj.getPersonName())) {
            PersonName personName = obj.getPersonName();
            subjectRef.setDisplay(personName.getFirstName()
                    + " " +
                    personName.getMiddleName() + " " +
                    personName.getLastName());
        }
        mr.setSubject(subjectRef);
        mr.setAuthoredOnElement(new DateTimeType("2146-07-21"));
        mr.addReasonCode(new CodeableConcept().setText(!ObjectUtils.isEmpty(obj.getReasonCode()) ? obj.getReasonCode() : ""));
        mr.addNote(new Annotation().setText(!ObjectUtils.isEmpty(obj.getPrescribedNote()) ? obj.getPrescribedNote() : ""));
        Dosage dosage = new Dosage();
        dosage.setText(obj.getProdStrength());
        Timing timing = new Timing();
        timing.getRepeat().setFrequency(2).setPeriod(1).setPeriodUnit(Timing.UnitsOfTime.D);
        dosage.setTiming(timing);
        dosage.setRoute(new CodeableConcept().addCoding(
                new Coding()
                        .setSystem("http://www.nlm.nih.gov/research/umls/rxnorm")
                        .setCode(!ObjectUtils.isEmpty(obj.getRoute()) ? obj.getRoute().toUpperCase() : "")
                        .setDisplay(!ObjectUtils.isEmpty(obj.getRoute()) ? obj.getRoute() : "")
        ));
        dosage.addDoseAndRate(new Dosage.DosageDoseAndRateComponent()
                .setDose(new Quantity()
                        .setValue((!ObjectUtils.isEmpty(obj.getDoseValRx()) ?
                                obj.getDoseValRx() : BigDecimal.ZERO))
                        .setUnit(!ObjectUtils.isEmpty(obj.getDoseUnitRx()) ? obj.getDoseUnitRx() : "")
                        .setSystem("http://www.nlm.nih.gov/research/umls/rxnorm")
                        .setCode(!ObjectUtils.isEmpty(obj.getFormUnitDisp()) ? "{" + obj.getFormUnitDisp() + "}" : "")));
        mr.addDosageInstruction(dosage);
        MedicationRequest.MedicationRequestDispenseRequestComponent dispense =
                new MedicationRequest.MedicationRequestDispenseRequestComponent();
        dispense.setQuantity(new Quantity()
                .setValue(5)
                .setUnit(!ObjectUtils.isEmpty(obj.getFormUnitDisp()) ? obj.getFormUnitDisp() : "")
                .setSystem("http://unitsofmeasure.org")
                .setCode(!ObjectUtils.isEmpty(obj.getFormUnitDisp()) ? "{" + obj.getFormUnitDisp() + "}" : ""));
        dispense.setExpectedSupplyDuration((Duration) new Duration()
                .setValue(10)
                .setUnit("days")
                .setSystem("http://unitsofmeasure.org")
                .setCode("d"));
        mr.setDispenseRequest(dispense);
        return mr;
    }


    public Bundle getPatientPrescription(Integer subjectId, Integer prescriptionId, String code, Pageable pageable) {

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setId(UUID.randomUUID().toString());
        bundle.setMeta(new Meta().setLastUpdated(new Date()));
        if (ObjectUtils.isEmpty(subjectId) && ObjectUtils.isEmpty(code) && ObjectUtils.isEmpty(prescriptionId)) {
            throw new FhirBadRequestException("In Prescription search params are missing, at least provide code!");
        }
        List<PersonPrescription> prescriptions =
                patientPrescriptionRepository.findBySubjectIdOrRowId(subjectId, prescriptionId, code,pageable);
        if (ObjectUtils.isEmpty(prescriptions)) {
            bundle.setTotal(0);
            return bundle;
        }
        bundle.setTotal(prescriptions.size());
        bundle.addLink()
                .setRelation("self")
                .setUrl("https://hapi.fhir.org/baseR4/MedicationRequest?subject=" + subjectId);
        for (PersonPrescription prescription : prescriptions) {

            MedicationRequest medicationRequest = mapToMedicationRequest(prescription);

            Bundle.BundleEntryComponent entry = bundle.addEntry();
            entry.setFullUrl("10.131.58.59:481/baseR4/MedicationRequest/" + subjectId);
            entry.setResource(medicationRequest);

            entry.setSearch(new Bundle.BundleEntrySearchComponent()
                    .setMode(Bundle.SearchEntryMode.MATCH));
        }
        return bundle;
    }

    //old snippet
    //    private Resource getResourceType(PersonPrescription obj) {
//        Resource resource = new Resource();
//        PersonName personName = obj.getPersonName();
//        resource.setId(obj.getRowId().toString());
//        resource.setResourceType("MedicationRequest");
//        resource.setMedicationCodeableConcept(MedicationCodeableConcept.builder()
//                .text(obj.getDrug()).coding(List.of(Coding.builder()
//                        .code(obj.getFormularyDrugCd())
//                        .system("http://www.nlm.nih.gov/research/umls/rxnorm")
//                        .display(obj.getDrug()).build())).build());
//        resource.setStatus(obj.getEndDate().isBefore(LocalDateTime.now())
//                ? "completed"
//                : "pending");
//        resource.setAuthoredOn(LocalDateTime
//                .parse(obj.getStartDate().toString())
//                .toLocalDate().toString());
//        resource.setDosageInstruction(List.of(DosageInstruction.builder()
//                .route(Route.builder()
//                        .coding(List.of(Coding.builder()
//                                .code(obj.getRoute())
//                                .system("http://www.nlm.nih.gov/research/umls/rxnorm")
//                                .display("Oral").build())).build())
//                .text(obj.getProdStrength())
//                .timing(Timing.builder()
//                        .repeat(Repeat.builder()
//                                .frequency(2)
//                                .period(1)
//                                .periodUnit("d").build()).build())
//                .doseAndRate(List.of(DoseAndRate.builder()
//                        .doseQuantity(DoseQuantity.builder()
//                                .value(0.58)
//                                .unit(obj.getDoseUnitRx())
//                                .code("{" + obj.getFormularyDrugCd() + "}")
//                                .system("http://www.nlm.nih.gov/research/umls/rxnorm").build())
//                        .build()))
//                .build()));
//        resource.setMeta(EntryMeta.builder().versionId("1")
//                .source("#ULtyC0aiSJxdl9E2")
//                .lastUpdated("2025-12-22T09:53:10.308+00:00").build());
//        resource.setIntent("order");
//        resource.setPriority("routine");
//        resource.setSubject(Subject.builder().display(!ObjectUtils.isEmpty(personName)
//                        ? personName.getFirstName() + " " + personName.getLastName() : "")
//                .reference("Patient/" + obj.getSubjectId()).build());
//        resource.setReasonCode(List.of(ReasonCode.builder().text("Fever and body pain").build()));
//        resource.setNote(List.of(Note.builder().text("Do not exceed recommended dose").build()));
//        resource.setDispenseRequest(DispenseRequest.builder()
//                .expectedSupplyDuration(ExpectedSupplyDuration.builder()
//                        .value(10)
//                        .code("d")
//                        .unit("days")
//                        .system("http://unitsofmeasure.org").build())
//                .quantity(ExpectedSupplyDuration.builder()
//                        .value(5)
//                        .code("{tbl}")
//                        .unit("tablet")
//                        .system("http://unitsofmeasure.org").build()).build());
//        return resource;
//    }
//
//    public PatientPrescriptionResponse getPatientPrescription(Integer subjectId, Integer prescriptionId) {
//        if (subjectId == null && prescriptionId == null) {
//            return null;
//        }
//        PatientPrescriptionResponse response = new PatientPrescriptionResponse();
//        List<PersonPrescription> bySubjectIdOrRowId =
//                patientPrescriptionRepository.findBySubjectIdOrRowId(subjectId, prescriptionId);
//        if (!ObjectUtils.isEmpty(bySubjectIdOrRowId)) {
//            response.setId(subjectId.toString());
//            response.setTotal(bySubjectIdOrRowId.size());
//            response.setLink(Link.builder().build());
//            response.setMeta(null);
//            List<Entry> entryList = bySubjectIdOrRowId.stream().map(ittr -> {
//                return Entry.builder().fullUrl("10.131.58.59:481/baseR4/MedicationRequest/" + subjectId)
//                        .resource(getResourceType(ittr))
//                        .search(Search.builder().mode("matched").build()).build();
//            }).toList();
//            response.setResourceType("Bundle");
//            response.setType("searchset");
//            response.setEntry(entryList);
//            response.setMeta(Meta.builder().lastUpdated("2025-12-26T05:53:23.339+00:00").build());
//            response.setLink(Link.builder().relation("self")
//                    .url("https://hapi.fhir.org/baseR4/MedicationRequest?subject=" + subjectId).build());
//        }
//        return response;
//    }
}
