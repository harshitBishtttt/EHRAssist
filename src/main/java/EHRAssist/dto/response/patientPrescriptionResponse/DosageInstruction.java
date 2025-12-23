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
public class DosageInstruction {
    private String text;
    private Timing timing;
    private Route route;
    private List<DoseAndRate> doseAndRate;
}
