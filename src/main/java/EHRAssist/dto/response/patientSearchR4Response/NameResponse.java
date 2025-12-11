package EHRAssist.dto.response.patientSearchR4Response;

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
    private String family;
    //private List<String> given;
}
