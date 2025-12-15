package EHRAssist.dto.response;

import EHRAssist.dto.response.observationResponse.DataMap;
import EHRAssist.dto.response.observationResponse.Subject;
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
    private Integer total;
    private Subject reference;
    private List<DataMap> dataMap;
}
