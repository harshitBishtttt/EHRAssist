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
@Table(name = "Visit_Admissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitAdmissions  {
    @Id
    private Integer rowId;

    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    @Column(name = "hadm_id", nullable = false)
    private Integer hadmId;

    @Column(name = "admittime", nullable = false)
    private LocalDateTime admitTime;

    @Column(name = "dischtime", nullable = false)
    private LocalDateTime dischargeTime;

    @Column(name = "deathtime")
    private LocalDateTime deathTime;

    @Column(name = "admission_type", length = 50, nullable = false)
    private String admissionType;

    @Column(name = "admission_location", length = 50, nullable = false)
    private String admissionLocation;

    @Column(name = "discharge_location", length = 50, nullable = false)
    private String dischargeLocation;

    @Column(name = "insurance", length = 50, nullable = false)
    private String insurance;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "religion", length = 50)
    private String religion;

    @Column(name = "marital_status", length = 50)
    private String maritalStatus;

    @Column(name = "ethnicity", length = 100, nullable = false)
    private String ethnicity;

    @Column(name = "edregtime")
    private LocalDateTime edRegTime;

    @Column(name = "edouttime")
    private LocalDateTime edOutTime;

    @Column(name = "diagnosis", length = 150, nullable = false)
    private String diagnosis;

    @Column(name = "hospital_expire_flag", nullable = false)
    private Boolean hospitalExpireFlag;

    @Column(name = "has_chartevents_data", nullable = false)
    private Boolean hasCharteventsData;
}
