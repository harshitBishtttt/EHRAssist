package EHRAssist.dto.response.patientPrescriptionResponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationCodeableConcept {
    private List<Coding> coding;
    private String text;
}
