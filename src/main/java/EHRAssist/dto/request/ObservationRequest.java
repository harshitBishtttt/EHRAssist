package EHRAssist.dto.request;

import EHRAssist.dto.request.observationMetaRequest.Code;
import EHRAssist.dto.request.observationMetaRequest.Specimen;
import EHRAssist.dto.request.observationMetaRequest.Subject;
import EHRAssist.dto.request.observationMetaRequest.ValueQuantity;
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
public class ObservationRequest {
    private Code code;
    private Subject subject;
    private List<String> itemId;
    private LocalDateTime effectiveDateTime;
    private Specimen specimen;
    private ValueQuantity valueQuantity;
}
