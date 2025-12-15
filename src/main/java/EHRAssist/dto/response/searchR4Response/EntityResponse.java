package EHRAssist.dto.response.searchR4Response;

import EHRAssist.dto.response.SearchResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityResponse {
    private String fullUrl;
    private ResourceResponse resource;
    private SearchResponse searchResponse;
}
