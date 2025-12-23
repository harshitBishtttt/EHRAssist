package EHRAssist.service;

import EHRAssist.dto.response.PatientPrescriptionResponse;

public interface PatientPrescriptionService {
    PatientPrescriptionResponse getPatientPrescription(Integer subjectId, Integer prescriptionId);
}
