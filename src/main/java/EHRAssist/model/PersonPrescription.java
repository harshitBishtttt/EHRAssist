package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JoinFormula;

import java.time.LocalDateTime;

@Entity
@Table(name = "Person_Prescriptions") // ⚠️ use your actual table name here
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonPrescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Integer rowId;

    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    @Column(name = "hadm_id", nullable = false)
    private Integer hadmId;

    @Column(name = "icustay_id")
    private Integer icustayId;

    @Column(name = "startdate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "enddate")
    private LocalDateTime endDate;

    @Column(name = "drug_type", nullable = false, length = 50)
    private String drugType;

    @Column(name = "drug", nullable = false, length = 50)
    private String drug;

    @Column(name = "drug_name_poe", length = 50)
    private String drugNamePoe;

    @Column(name = "drug_name_generic", length = 50)
    private String drugNameGeneric;

    @Column(name = "formulary_drug_cd", length = 50)
    private String formularyDrugCd;

    @Column(name = "gsn")
    private Integer gsn;

    @Column(name = "ndc")
    private Long ndc;

    @Column(name = "prod_strength", nullable = false, length = 100)
    private String prodStrength;

    @Column(name = "dose_val_rx", nullable = false, length = 50)
    private String doseValRx;

    @Column(name = "dose_unit_rx", nullable = false, length = 50)
    private String doseUnitRx;

    @Column(name = "form_val_disp", length = 50)
    private String formValDisp;

    @Column(name = "form_unit_disp", length = 50)
    private String formUnitDisp;

    @Column(name = "route", nullable = false, length = 50)
    private String route;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula("""
               (SELECT pn.id
                FROM Person_Name pn
                WHERE pn.subject_id_from_person_table = subject_id
                  AND pn.name_type = 'official')
            """)
    private PersonName personName;

}
