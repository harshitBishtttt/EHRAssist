package EHRAssist.dto.response.patientSearchResponse;

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
public class Resource {
    private String resourceType;
    private String id;
    private EntryMeta meta;
    private Text text;
    //private Boolean active;
    private List<NameResponse> name;
    private List<Object> telecom;
    private String gender;
    private LocalDate birthDate;
    private List<Address> address;
    private MaritalStatus maritalStatus;
    private List<Communication> communication;
    private List<Extension> extension;


}
