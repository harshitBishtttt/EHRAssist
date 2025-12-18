package EHRAssist.controller;

import EHRAssist.dto.response.PatientConditionResponse;
import EHRAssist.dto.response.PatientEncounterResponse;
import EHRAssist.dto.response.PatientProcedureResponse;
import EHRAssist.dto.response.PatientSearchResponse;
import EHRAssist.service.PatientConditionService;
import EHRAssist.service.PatientEncounterService;
import EHRAssist.service.PatientProceduresService;
import EHRAssist.service.PatientSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/baseR4")
public class PatientController {

    @Autowired
    private PatientSearchService patientSearchService;
    @Autowired
    private PatientConditionService patientConditionService;
    @Autowired
    private PatientProceduresService patientProceduresService;
    @Autowired
    private PatientEncounterService patientEncounterService;


    @GetMapping("/Patient")
    ResponseEntity<PatientSearchResponse> searchPatient(
            @RequestParam(required = false, defaultValue = "") String family,
            @RequestParam(required = false, defaultValue = "") String given,
            @RequestParam(required = false, defaultValue = "") String email,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdate,
            @RequestParam(required = false, defaultValue = "") String gender,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                patientSearchService.searchPatient(family, given, email, birthdate, gender, pageable)
        );
    }

    @GetMapping("/Condition")
    ResponseEntity<PatientConditionResponse> getPatientCondition(
            @RequestParam(required = false, defaultValue = "") Integer subject,
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false) Integer encounter,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                patientConditionService.getPatientCondition(subject, code, encounter, pageable)
        );
    }


    @GetMapping("/Procedure")
    ResponseEntity<PatientProcedureResponse> getPatientProcedure(
            @RequestParam(required = false) Integer subject,
            @RequestParam(required = false) Integer encounter,
            @RequestParam(required = false) Integer code,
            Pageable pageable) {
        return ResponseEntity.ok(patientProceduresService.getPatientProcedure(subject, encounter, code, pageable));
    }

    @GetMapping("/Encounter")
    ResponseEntity<PatientEncounterResponse> getPatientEncounter(
            @RequestParam(required = false) Integer subject,
            @RequestParam(required = false) Integer count,
            Pageable pageable) {
        return ResponseEntity.ok(patientEncounterService.getPatientEncounter(subject, count, pageable));
    }


}
