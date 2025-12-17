package EHRAssist.controller;

import EHRAssist.dto.response.PatientConditionResponse;
import EHRAssist.dto.response.PatientSearchResponse;
import EHRAssist.service.PatientConditionService;
import EHRAssist.service.PatientSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/baseR4")
public class PatientSearchController {

    @Autowired
    private PatientSearchService patientSearchService;
    @Autowired
    private PatientConditionService patientConditionService;


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
    ResponseEntity<PatientConditionResponse> getPersonCondition(
            @RequestParam(required = false, defaultValue = "") Integer subject,
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false) Integer encounter,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                patientConditionService.getPersonCondition(subject, code, encounter, pageable)
        );
    }


//    @PostMapping("/person-observation")
//    ResponseEntity<PersonObservationResponse> getPersonObservations(@RequestBody ObservationRequest request, Pageable pageable) {
//        return ResponseEntity.ok(observationsService.getPersonObservations(request, pageable));
//    }


}
