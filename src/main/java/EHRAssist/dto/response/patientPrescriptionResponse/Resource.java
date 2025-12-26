package EHRAssist.dto.response.patientPrescriptionResponse;

import EHRAssist.dto.response.patientSearchResponse.EntryMeta;
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
    private Integer id;
    private EntryMeta meta;
    private String status;
    private String intent;
    private String priority;
    private MedicationCodeableConcept medicationCodeableConcept;
    private Subject subject;
    private String authoredOn;
    private List<ReasonCode> reasonCode;
    private List<Note> note;
    private DosageInstruction dosageInstruction;
    private DispenseRequest dispenseRequest;
}
