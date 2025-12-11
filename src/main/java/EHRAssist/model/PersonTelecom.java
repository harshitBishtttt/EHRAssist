package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Person_Telecom")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonTelecom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   // PK auto-increment

    @Column(name = "system")
    private String system;   // phone | email | fax | pager | url | sms | other
    @Column(name = "value")
    private String value;    // actual contact detail (phone number, email, etc.)
    @Column(name = "use_tel")
    private String useTel;
}
