package EHRAssist.controller;

import EHRAssist.dto.request.ObservationRequest;
import EHRAssist.dto.response.PatientSearchResponse;
import EHRAssist.dto.response.PersonObservationResponse;
import EHRAssist.service.impls.ObservationServiceImpl;
import EHRAssist.service.impls.PatientSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/baseR4/Patient")
public class PatientSearchController {

    @Autowired
    private PatientSearchServiceImpl patientService;
    @Autowired
    private ObservationServiceImpl observationsService;

//    @GetMapping
//    ResponseEntity<PatientSearchResponse> searchPatientByEmail(@RequestParam String email, Pageable pageable) {
//        return ResponseEntity.ok(patientService.searchPatientByEmail(email, pageable));
//    }

    @GetMapping
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
                patientService.searchPatient(family, given, email, birthdate, gender, pageable)
        );
    }

    @PostMapping("/person-observation")
    ResponseEntity<PersonObservationResponse> getPersonObservations(@RequestBody ObservationRequest request, Pageable pageable) {
        return ResponseEntity.ok(observationsService.getPersonObservations(request, pageable));
    }


}
