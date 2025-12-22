package EHRAssist.dto.request.observationMetaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    private String coding;
    //ionic code cardinality is 1..1 only one per observation cant be zero
}
