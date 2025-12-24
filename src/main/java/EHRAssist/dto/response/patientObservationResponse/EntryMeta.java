package EHRAssist.dto.response.patientObservationResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryMeta {
    private String versionId;
    private String lastUpdated;
    private String source;
    private List<String> profile;
}
