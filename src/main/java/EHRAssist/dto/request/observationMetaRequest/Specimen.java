package EHRAssist.dto.request.observationMetaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Specimen {
    private String identifier; //category of liquid cardinality is 0..1 only one per observations
}
