package EHRAssist.service;

import EHRAssist.dto.response.PatientConditionResponse;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.Pageable;


public interface PatientConditionService {
    Bundle getPatientCondition(Integer subject, String code, Integer encounter, Pageable pageable);
}
