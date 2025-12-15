package EHRAssist.controller;

import EHRAssist.dto.request.ObservationRequest;
import EHRAssist.dto.request.PersonRequest;
import EHRAssist.dto.response.PersonsResponse;
import EHRAssist.dto.response.observationResponse.PersonObservationResponse;
import EHRAssist.service.ObservationService;
import EHRAssist.service.PersonSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ehrA/fhir")
public class PersonController {

    @Autowired
    private PersonSearchService patientService;
    @Autowired
    private ObservationService observationsService;

    @PostMapping("/person-search")
    ResponseEntity<PersonsResponse> searchPerson(@RequestBody PersonRequest request, Pageable pageable) {
        return ResponseEntity.ok(patientService.searchPerson(request, pageable));
    }

    @PostMapping("/person-observation")
    ResponseEntity<PersonObservationResponse> getPersonObservations(@RequestBody ObservationRequest request, Pageable pageable) {
        return ResponseEntity.ok(observationsService.getPersonObservations(request, pageable));
    }


}
