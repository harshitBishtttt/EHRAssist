package EHRAssist.dto.response.patientConditionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String resourceType;
    private Integer id;
    private EntryMeta meta;
    private ClinicalStatus clinicalStatus;
    private VerificationStatus verificationStatus;
    private Category category;
    private Severity severity;
    private Code code;
    private Subject subject;
    private Encounter encounter;
    private LocalDateTime recordedDate;
}
