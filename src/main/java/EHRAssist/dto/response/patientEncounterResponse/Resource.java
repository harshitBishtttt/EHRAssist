package EHRAssist.dto.response.patientEncounterResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String resourceType;
    private String id;
    private EntryMeta meta;
    private List<Extension> extension;
    private String status;
    private Classs class_;
    private ResourceType type;
    private Subject subject;
    private Period period;
}
