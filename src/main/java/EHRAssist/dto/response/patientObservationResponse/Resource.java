package EHRAssist.dto.response.patientObservationResponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Identifier identifier;
    private String status;
    private List<Category> category;
    private Code code;
    private Subject subject;
    private Encounter encounter;
    private String effectiveDateTime;
    private String issued;
    private List<Interpretation> interpretation;
    private List<Note> note;
    private BodySite bodySite;
    private Method method;
    private List<Component> component;
}
