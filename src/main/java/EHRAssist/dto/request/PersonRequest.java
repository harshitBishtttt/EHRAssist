package EHRAssist.dto.request;

import EHRAssist.dto.request.personMetaRequest.AddressInfoRequest;
import EHRAssist.dto.request.personMetaRequest.ExtensionsInfoRequest;
import EHRAssist.dto.request.personMetaRequest.NameInfoRequest;
import EHRAssist.dto.request.personMetaRequest.TelecomInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {
    private String resourceType;
    private NameInfoRequest nameRequest;
    private AddressInfoRequest address;
    private TelecomInfoRequest telecom;
    private LocalDate birthdate;
    private String gender;
    private String identifier;
    private ExtensionsInfoRequest extensionsInfoRequest;

}
