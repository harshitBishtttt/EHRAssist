package EHRAssist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Procedure_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureMaster {
    @Id
    @Column(name = "row_id", nullable = false)
    private Integer rowId;

    @Column(name = "category", nullable = false)
    private Integer category;

    @Column(name = "sectionrange", length = 50, nullable = false)
    private String sectionRange;

    @Column(name = "sectionheader", length = 50, nullable = false)
    private String sectionHeader;

    @Column(name = "subsectionrange", length = 50, nullable = false)
    private String subsectionRange;

    @Column(name = "subsectionheader", length = 200, nullable = false)
    private String subsectionHeader;

    @Column(name = "codesuffix")
    private Boolean codeSuffix;

    @Column(name = "mincodeinsubsection", nullable = false)
    private Integer minCodeInSubsection;

    @Column(name = "maxcodeinsubsection", nullable = false)
    private Integer maxCodeInSubsection;
}
