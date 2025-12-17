package EHRAssist.dto.response.personProcedureResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    private List<Coding> coding;
    private String text;

}
