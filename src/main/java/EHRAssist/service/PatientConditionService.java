package EHRAssist.service;

import EHRAssist.dto.response.PatientConditionResponse;
import EHRAssist.dto.response.PatientSearchResponse;
import org.springframework.data.domain.Pageable;


public interface PatientConditionService {
    PatientConditionResponse getPersonCondition(Integer subject, String code, Integer encounter, Pageable pageable);
}
