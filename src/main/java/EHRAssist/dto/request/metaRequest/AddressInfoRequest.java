package EHRAssist.dto.request.metaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfoRequest {
    private String address;
    private String addressCity;
    private String addressPostalCode;
    private String addressState;
}
