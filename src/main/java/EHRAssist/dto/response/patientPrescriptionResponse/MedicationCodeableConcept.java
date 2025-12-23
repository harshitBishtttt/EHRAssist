package EHRAssist.dto.response.patientPrescriptionResponse;

import EHRAssist.dto.response.patientObservationResponse.Coding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationCodeableConcept {
    private Coding coding;
    private String text;
}
