package EHRAssist.dto.request.metaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionsInfoRequest {  //These are referred as non-mandatory or optionals parameters
    private String ownPrefix;
    private String partnerName;
    private String partnerPrefix;
    private String legalSex;
}
