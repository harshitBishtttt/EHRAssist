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
    private String firstName;
    private String middleName;
    private String lastName;
}
