package EHRAssist.service;

import EHRAssist.dto.response.PatientObservationResponse;
import org.springframework.data.domain.Pageable;

public interface PatientObservationService {
    PatientObservationResponse getPatientObservations(Integer subject,
                                                      String code,
                                                      Integer encounter,
                                                      Pageable pageable);

}

