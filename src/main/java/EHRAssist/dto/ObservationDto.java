package EHRAssist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObservationDto {
    private Integer subject;
    private Integer encounter;
    private FhirQuantity fhirQuantity;
    private String code;

}
