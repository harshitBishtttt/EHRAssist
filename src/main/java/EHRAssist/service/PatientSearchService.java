package EHRAssist.service;

import EHRAssist.dto.response.PatientSearchResponse;
import org.springframework.data.domain.Pageable;

public interface PatientSearchService {
    PatientSearchResponse searchPatientByEmail(String email, Pageable pageable);
}
