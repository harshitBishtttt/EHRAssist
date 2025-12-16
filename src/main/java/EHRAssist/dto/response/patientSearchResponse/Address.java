package EHRAssist.dto.response.patientSearchResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private List<String> line;
    private String city;
    private String state;
    private String postalCode;
}
