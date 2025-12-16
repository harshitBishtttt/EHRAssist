package EHRAssist.dto.response.patientSearchResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameResponse {
    private String use;
    private List<String> given;
    private String family;
}
