package EHRAssist.dto.response.personProcedureResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coding {
    private String system;
    private String code;
    private String display;
}
