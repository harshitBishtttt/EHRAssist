package EHRAssist.service;

import EHRAssist.dto.response.PatientProcedureResponse;
import org.springframework.data.domain.Pageable;

public interface PatientProceduresService {
    PatientProcedureResponse getPatientProcedure(Integer subject, Integer encounter, Integer code, Pageable pageable);

}
