package EHRAssist.dto.response.patientEncounterResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntryMeta {
    private String versionId;
    private String lastUpdate;
    private String source;
}
