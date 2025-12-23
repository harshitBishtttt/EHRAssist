package EHRAssist.dto.response.patientPrescriptionResponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispenseRequest {
    private ExpectedSupplyDuration quantity;
    private ExpectedSupplyDuration expectedSupplyDuration;
}
