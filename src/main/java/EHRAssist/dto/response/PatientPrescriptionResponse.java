package EHRAssist.dto.response;

import EHRAssist.dto.response.patientConditionResponse.Meta;
import EHRAssist.dto.response.patientPrescriptionResponse.Entry;
import EHRAssist.dto.response.patientSearchResponse.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientPrescriptionResponse {
    private String resourceType;
    private String id;
    private String type;
    private Meta meta;
    private Integer total;
    private Link link;
    private List<Entry> entry;
}
