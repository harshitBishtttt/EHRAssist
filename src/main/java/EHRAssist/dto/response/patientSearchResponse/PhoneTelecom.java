package EHRAssist.dto.response.patientSearchResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneTelecom {
    private String system;
    private String value;
    private String use;
}
