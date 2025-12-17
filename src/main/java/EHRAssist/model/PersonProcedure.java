package EHRAssist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Person_Procedure")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonProcedure {
    @Id
    @Column(name = "row_id", nullable = false)
    private Integer rowId;

    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    @Column(name = "hadm_id", nullable = false)
    private Integer hadmId;

    @Column(name = "costcenter", length = 50, nullable = false)
    private String costCenter;

    @Column(name = "chartdate")
    private LocalDateTime chartDate;

    @Column(name = "cpt_cd", nullable = false)
    private Integer cptCd;

    @Column(name = "cpt_number", nullable = false)
    private Integer cptNumber;

    @Column(name = "cpt_suffix", length = 1)
    private String cptSuffix;

    @Column(name = "ticket_id_seq")
    private Integer ticketIdSeq;

    @Column(name = "sectionheader", length = 100, nullable = false)
    private String sectionHeader;

    @Column(name = "subsectionheader", length = 200, nullable = false)
    private String subsectionHeader;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "source_file", length = 50)
    private String sourceFile;

    @Column(name = "load_batch_id", length = 50)
    private String loadBatchId;

    @Column(name = "loaded_at")
    private LocalDateTime loadedAt;
}
