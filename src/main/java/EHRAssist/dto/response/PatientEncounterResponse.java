package EHRAssist.dto.response;

import EHRAssist.dto.response.patientConditionResponse.Link;
import EHRAssist.dto.response.patientConditionResponse.Meta;
import EHRAssist.dto.response.patientEncounterResponse.Entry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientEncounterResponse {
    private String resourceType;
    private String id;
    private Meta meta;
    private String type;
    private Integer total;
    private List<Link> link;
    private List<Entry> entry;
}
