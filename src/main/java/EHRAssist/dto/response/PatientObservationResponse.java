package EHRAssist.dto.response;

import EHRAssist.dto.response.patientConditionResponse.Link;
import EHRAssist.dto.response.patientObservationResponse.Entry;
import EHRAssist.dto.response.patientObservationResponse.Meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientObservationResponse {
    private String resourceType;
    private String id;
    private String type;
    private Integer total;
    private Meta meta;
    private Link link;
    List<Entry> entry;
}
