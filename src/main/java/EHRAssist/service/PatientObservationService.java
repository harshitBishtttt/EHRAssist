package EHRAssist.service;

import EHRAssist.dto.response.PatientObservationResponse;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.Pageable;

public interface PatientObservationService {
    Bundle getPatientObservations(Integer subject,
                                  String code,
                                  Integer encounter,
                                  Pageable pageable);

}

