package EHRAssist.dto.response.patientPrescriptionResponse;

import EHRAssist.dto.response.patientConditionResponse.Coding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    private List<Coding> rout;
}
