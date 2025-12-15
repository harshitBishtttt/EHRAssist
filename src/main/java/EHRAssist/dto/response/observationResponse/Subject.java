package EHRAssist.dto.response.observationResponse;

import EHRAssist.dto.response.searchR4Response.NameResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private Integer id;
    private List<NameResponse> names;
}
