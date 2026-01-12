package EHRAssist.service;

import EHRAssist.dto.response.PatientSearchResponse;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PatientSearchService {
    //PatientSearchResponse searchPatientByEmail(String email, Pageable pageable);

    Bundle searchPatient(String family,
                         String given,
                         String email,
                         String phone,
                         LocalDate birthdate,
                         String gender,
                         Pageable pageable);
}
