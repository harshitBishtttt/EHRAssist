package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Person_Name")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "family")
    private String family;   // surname

    @Column(name = "given")
    private String given;
}
