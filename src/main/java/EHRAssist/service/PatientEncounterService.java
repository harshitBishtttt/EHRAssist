package EHRAssist.service;

import EHRAssist.dto.response.PatientEncounterResponse;
import org.springframework.data.domain.Pageable;

public interface PatientEncounterService {
    PatientEncounterResponse getPatientEncounter(Integer subject, Integer count,
                                                 Pageable pageable);



}
