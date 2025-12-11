package EHRAssist.dto.request.metaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameInfoRequest {
    private String family;
    private String given;
    private String name;
    private String ownName;
}
