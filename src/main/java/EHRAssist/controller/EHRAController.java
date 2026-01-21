package EHRAssist.controller;

import EHRAssist.service.*;
import EHRAssist.utils.EHRAUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/baseR4")
public class EHRAController {

    @Autowired
    private PatientSearchService patientSearchService;
    @Autowired
    private PatientConditionService patientConditionService;
    @Autowired
    private PatientProceduresService patientProceduresService;
    @Autowired
    private PatientEncounterService patientEncounterService;
    @Autowired
    private PatientObservationService getPatientObservations;
    @Autowired
    private PatientPrescriptionService patientPrescriptionService;
    @Autowired
    private EHRAUtils ehraUtils;


    @GetMapping("/Patient")
    ResponseEntity<String> searchPatient(
            @RequestParam(required = false, defaultValue = "") String family,
            @RequestParam(required = false, defaultValue = "") String given,
            @RequestParam(required = false, defaultValue = "") String email,
            @RequestParam(required = false) String phone,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdate,
            @RequestParam(required = false, defaultValue = "") String gender,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Bundle response = patientSearchService.searchPatient(family, given, email, phone, birthdate, gender, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }

    @GetMapping("/Condition")
    ResponseEntity<String> getPatientCondition(
            @RequestParam(required = false) Integer subject,
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false) Integer encounter,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Bundle response = patientConditionService.getPatientCondition(subject, code, encounter, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }


    @GetMapping("/Procedure")
    ResponseEntity<String> getPatientProcedure(
            @RequestParam(required = false) Integer subject,
            @RequestParam(required = false) Integer encounter,
            @RequestParam(required = false) Integer code,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Bundle response = patientProceduresService.getPatientProcedure(subject, encounter, code, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }

    @GetMapping("/Encounter")
    ResponseEntity<String> getPatientEncounter(
            @RequestParam Integer subject,
            @RequestParam(required = false) Integer count,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Bundle response = patientEncounterService.getPatientEncounter(subject, count, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }

    @GetMapping("/Observations")
    ResponseEntity<String> getPatientObservations(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String code,
            @RequestParam(name = "value-quantity", required = false) String valueQuantity,
            @RequestParam(required = false) String encounter,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Bundle response = getPatientObservations.getPatientObservations(subject, code, valueQuantity, encounter, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }

    @GetMapping("/MedicationRequest")
    ResponseEntity<String> getPatientPrescription(@RequestParam(required = false) Integer subject,
                                                  @RequestParam(required = false) Integer prescriptionId,
                                                  @RequestParam(required = false) String code,
                                                  @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Bundle response = patientPrescriptionService.getPatientPrescription(subject, prescriptionId, code,pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }


}
