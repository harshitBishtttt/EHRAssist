package EHRAssist.dto.response.patientConditionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String resourceType;
    private String id;
    private EntryMeta meta;
    private ClinicalStatus clinicalStatus;
    private VerificationStatus verificationStatus;
    private List<Category> category;
    private Severity severity;
    private Code code;
    private Subject subject;
    private Encounter encounter;
    private LocalDateTime recordedDate;


}
