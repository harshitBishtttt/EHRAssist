package EHRAssist.service;

import EHRAssist.dto.response.PatientSearchResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PatientSearchService {
    PatientSearchResponse searchPatientByEmail(String email, Pageable pageable);

    PatientSearchResponse searchPatient(String family,
                                        String given,
                                        String email,
                                        LocalDate birthdate,
                                        String gender,
                                        Pageable pageable);
}
