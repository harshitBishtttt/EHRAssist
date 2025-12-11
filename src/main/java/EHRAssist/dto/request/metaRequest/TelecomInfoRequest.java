package EHRAssist.dto.request.metaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelecomInfoRequest {
    private String system;
    private String value;
    private String useTel;
}
