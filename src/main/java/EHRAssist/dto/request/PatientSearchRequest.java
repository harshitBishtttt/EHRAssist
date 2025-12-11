package EHRAssist.dto.request;

import EHRAssist.dto.request.metaRequest.AddressInfoRequest;
import EHRAssist.dto.request.metaRequest.ExtensionsInfoRequest;
import EHRAssist.dto.request.metaRequest.NameInfoRequest;
import EHRAssist.dto.request.metaRequest.TelecomInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientSearchRequest {
    private NameInfoRequest nameRequest;
    private AddressInfoRequest address;
    private TelecomInfoRequest telecom;
    private LocalDate birthdate;
    private String gender;
    private String identifier;
    private ExtensionsInfoRequest extensionsInfoRequest;

}
