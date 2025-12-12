package EHRAssist.dto.response.patientSearchR4Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {
    private String resourceType;
    private Integer id;
    private List<IdentifierResponse> identifier;
    private List<NameResponse> name;
    private List<TelecomResponse> telecom;
    private String gender;
    private LocalDate birthDate;
    private List<AddressResponse> address;
    private List<ExtensionResponse> extension;
}
