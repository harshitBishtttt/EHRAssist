package EHRAssist.dto.response.searchR4Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtensionResponse {
    private String url;
    private String valueCode;

}
