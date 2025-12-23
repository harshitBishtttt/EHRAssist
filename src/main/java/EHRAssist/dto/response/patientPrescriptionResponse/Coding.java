package EHRAssist.dto.response.patientPrescriptionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coding {
    private String system;
    private String code;
    private String display;
}
