package EHRAssist.dto.response.observationResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataMap {
    private Coding coding;
    private String encounter;
    private String specimen;
    private LocalDateTime collectedDateTime;
    private ValueQuantity valueQuantity;
    private Interpretation interpretation;
}
