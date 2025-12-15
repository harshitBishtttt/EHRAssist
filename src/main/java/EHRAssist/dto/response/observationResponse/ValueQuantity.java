package EHRAssist.dto.response.observationResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueQuantity {
    private Double value;
    private String unit;
    private String system;
    private String code;
}
