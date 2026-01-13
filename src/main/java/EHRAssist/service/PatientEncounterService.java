package EHRAssist.service;

import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.Pageable;

public interface PatientEncounterService {
    Bundle getPatientEncounter(Integer subject, Integer count,
                               Pageable pageable);


}
