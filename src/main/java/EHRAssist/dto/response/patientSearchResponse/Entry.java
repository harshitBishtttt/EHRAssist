package EHRAssist.dto.response.patientSearchResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entry {
    private String fullUrl;
    private Resource resource;
    private SearchResponse mode;
}
