package EHRAssist.service;

import EHRAssist.dto.response.PatientPrescriptionResponse;
import org.hl7.fhir.r4.model.Bundle;

public interface PatientPrescriptionService {
    Bundle getPatientPrescription(Integer subjectId, Integer prescriptionId);
}
