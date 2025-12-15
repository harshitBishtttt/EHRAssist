package EHRAssist.dto.response;

import EHRAssist.dto.response.observationResponse.Entry;
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
    List<Entry> entry;
}
