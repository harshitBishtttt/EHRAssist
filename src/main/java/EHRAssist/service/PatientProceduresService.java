package EHRAssist.service;

import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.Pageable;

public interface PatientProceduresService {
    Bundle getPatientProcedure(Integer subject, Integer encounter, Integer code, Pageable pageable);

}
