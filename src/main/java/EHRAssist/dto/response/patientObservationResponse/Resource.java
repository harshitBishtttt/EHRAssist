package EHRAssist.dto.response.patientObservationResponse;

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
    private String status;
    private Code code;
    private Subject subject;
    private LocalDateTime effectiveDateTime;
    private ValueQuantity valueQuantity;

    private Specimen specimen;
    private String encounter;
}
