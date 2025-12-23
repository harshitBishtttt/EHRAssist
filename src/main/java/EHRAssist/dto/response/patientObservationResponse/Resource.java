package EHRAssist.dto.response.patientObservationResponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String resourceType;
    private Integer id;
    private Meta meta;
    private Text text;
    private Identifier identifier;
    private String status;
    private Category category;
    private Code code;
    private Subject subject;
    private LocalDateTime effectiveDateTime;
    private Note note;
    private Device device;
    private List<Component> component;
    private String encounter;
}
