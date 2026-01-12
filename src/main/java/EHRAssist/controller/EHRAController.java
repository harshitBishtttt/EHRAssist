package EHRAssist.controller;

import EHRAssist.dto.response.*;
import EHRAssist.service.*;
import EHRAssist.utils.EHRAUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Pageable pageable
    ) {
        Bundle response = patientSearchService.searchPatient(family, given, email, phone, birthdate, gender, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }

    @GetMapping("/Condition")
    ResponseEntity<String> getPatientCondition(
            @RequestParam Integer subject,
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false) Integer encounter,
            Pageable pageable
    ) {
        Bundle response = patientConditionService.getPatientCondition(subject, code, encounter, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }


    @GetMapping("/Procedure")
    ResponseEntity<String> getPatientProcedure(
            @RequestParam Integer subject,
            @RequestParam(required = false) Integer encounter,
            @RequestParam(required = false) Integer code,
            Pageable pageable) {
        Bundle response = patientProceduresService.getPatientProcedure(subject, encounter, code, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }

    @GetMapping("/Encounter")
    ResponseEntity<PatientEncounterResponse> getPatientEncounter(
            @RequestParam Integer subject,
            @RequestParam(required = false) Integer count,
            Pageable pageable) {
        return ResponseEntity.ok(patientEncounterService.getPatientEncounter(subject, count, pageable));
    }

    @GetMapping("/Observations")
    ResponseEntity<String> getPatientObservations(
            @RequestParam Integer subject,
            @RequestParam(required = false) Integer patient,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer encounter,
            Pageable pageable) {
        Bundle response = getPatientObservations.getPatientObservations(subject, code, encounter, pageable);
        return ehraUtils.fhirResponseWrapper(response);
    }

    @GetMapping("/MedicationRequest")
    ResponseEntity<PatientPrescriptionResponse> getPatientPrescription(@RequestParam Integer subject,
                                                                       @RequestParam(required = false) Integer prescriptionId) {
        return ResponseEntity.ok(patientPrescriptionService.getPatientPrescription(subject, prescriptionId));
    }


}
