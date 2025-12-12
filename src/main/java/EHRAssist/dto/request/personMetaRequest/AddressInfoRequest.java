package EHRAssist.dto.request.personMetaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfoRequest {
    private String addressOne;
    private String addressTwo;
    private String addressThree;
    private String postalCode;
    private String country;
}
