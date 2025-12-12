package EHRAssist.controller;

import EHRAssist.dto.request.PatientSearchRequest;
import EHRAssist.dto.response.PersonsResponse;
import EHRAssist.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instance/api/FHIR/R4")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/person-search")
    ResponseEntity<PersonsResponse> searchPerson(@RequestBody PatientSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(patientService.searchPerson(request,pageable));
    }


}
