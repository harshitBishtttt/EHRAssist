package EHRAssist.service;

import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.Pageable;

public interface PatientPrescriptionService {
    Bundle getPatientPrescription(Integer subjectId, Integer prescriptionId, String code, Pageable pageable);
}
