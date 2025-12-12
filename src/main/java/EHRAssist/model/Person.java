package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private Short rowId;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "dob")
    private LocalDateTime dob;

    @Column(name = "dod")
    private LocalDateTime dod;

    @Column(name = "dod_hosp")
    private LocalDateTime dodHosp;

    @Column(name = "dod_ssn")
    private LocalDateTime dodSsn;

    @Column(name = "expire_flag")
    private Byte expireFlag;

    @Column(name = "source_file")
    private String sourceFile;

    @Column(name = "load_batch_id")
    private String loadBatchId;

    @Column(name = "loaded_at")
    private LocalDateTime loadedAt;
    @Column(name = "birth_date")
    private LocalDate birthdate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id_from_person_table")
    @BatchSize(size = 20)
    private Set<PersonTelecom> patientTelecom;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id_from_person_table")
    @BatchSize(size = 20)
    private Set<PersonAddress> personAddress;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id_from_person_table")
    @BatchSize(size = 20)
    private Set<PersonName> personName;

}
