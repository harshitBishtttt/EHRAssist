package EHRAssist.dto.response;

import EHRAssist.dto.response.patientSearchResponse.Entry;
import EHRAssist.dto.response.patientSearchResponse.Link;
import EHRAssist.dto.response.patientSearchResponse.Meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientSearchResponse {
    private String resourceType;
    private String id;
    private Meta meta;
    private String type;
    private Integer total;
    private List<Link> link;
    List<Entry> entry;

}
