package EHRAssist.dto.response;

import EHRAssist.dto.response.patientSearchR4Response.EntityResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonsResponse {
    private String resourceType;
    private String type;
    private Integer total;
    List<EntityResponse> entry;

}
