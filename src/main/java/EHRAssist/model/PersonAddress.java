package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Person_Address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;   // PK auto-increment

    @Column(name = "address_one", columnDefinition = "NVARCHAR(MAX)")
    private String addressOne;         // full address lines (can store JSON or comma-separated list)

    @Column(name = "address_two")
    private String addressTwo;

    @Column(name = "address_three")
    private String addressThree;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;
}
