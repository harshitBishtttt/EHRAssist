package EHRAssist.service;

import EHRAssist.dto.response.PatientConditionResponse;
import org.springframework.data.domain.Pageable;


public interface PatientConditionService {
    PatientConditionResponse getPatientCondition(Integer subject, String code, Integer encounter, Pageable pageable);
}
