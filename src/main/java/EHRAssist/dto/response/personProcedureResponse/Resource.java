package EHRAssist.dto.response.personProcedureResponse;

import EHRAssist.dto.response.patientConditionResponse.EntryMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String resourceType;
    private Integer id;
    private EntryMeta meta;
    private String status;
    private Category category;
    private Code code;
    private Subject subject;
    private Encounter encounter;
}
