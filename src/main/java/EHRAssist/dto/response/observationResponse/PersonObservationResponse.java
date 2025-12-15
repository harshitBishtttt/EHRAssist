package EHRAssist.dto.response.observationResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonObservationResponse {
    private String resourceType;
    private String type;
    private Integer total;
    private List<Patient> patient;
    private List<Test> tests;
}
