package EHRAssist.dto.response.patientPrescriptionResponse;

import EHRAssist.dto.response.personProcedureResponse.Search;
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
    private Search search;
}
