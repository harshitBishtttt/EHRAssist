package EHRAssist.dto.response.patientEncounterResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private List<Object> extension;
    private String status;
    @JsonProperty("class")
    private Classs classs;
    private List<ResourceType> type;
    private Subject subject;
    private Period period;
}
