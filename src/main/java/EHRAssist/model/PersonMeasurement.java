package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Person_Measurement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Integer rowId;

    @Column(name = "subject_id")
    private Integer subjectId;


    @Column(name = "hadm_id")
    private Integer hadmId;

    @Column(name = "charttime")
    private LocalDateTime chartTime;

    @Column(name = "value")
    private Float value;

    @Column(name = "valuenum")
    private Integer valueNum;

    @Column(name = "valueuom")
    private Integer valueUom;

    @Column(name = "flag")
    private String flag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemid", referencedColumnName = "itemid")
    private MeasurementMaster measurementMaster;
}
