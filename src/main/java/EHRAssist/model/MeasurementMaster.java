package EHRAssist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "Measurement_Master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Integer rowId;
    @Column(name = "itemid", nullable = false)
    private Integer itemId;

    @Column(name = "label")
    private String label;
    @Column(name = "fluid")
    private String fluid;

    @Column(name = "category")
    private String category;

    @Column(name = "loinc_code")
    private String loincCode;

    @OneToMany(mappedBy = "measurementMaster", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PersonMeasurement> personMeasurements;
}
