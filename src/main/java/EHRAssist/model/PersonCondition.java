package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Person_Condition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonCondition {

    @Id
    @Column(name = "row_id")
    private Integer rowId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id")
    private Person person;

    @Column(name = "hadm_id")
    private Integer hadmId;

    @Column(name = "seq_num")
    private Integer seqNum;
    @Column(name = "severity_code")
    private String severityCode;

    @Column(name = "severity")
    private String severity;


    @OneToOne
    @JoinColumn(name = "icd9_code")
    private ConditionMaster conditionMaster;

}
