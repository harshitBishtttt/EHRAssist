package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Person_Language")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "language_code")
    private String languageCode;
    @Column(name = "language_name")
    private String languageName;
    @Column(name = "active")
    private Boolean active;
}
