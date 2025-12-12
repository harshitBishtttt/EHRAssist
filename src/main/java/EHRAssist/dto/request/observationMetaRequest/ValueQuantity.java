package EHRAssist.dto.request.observationMetaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueQuantity {
    private Integer value;
    private String unit;
    private String comparator;
}
