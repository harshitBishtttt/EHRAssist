package EHRAssist.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Condition_Master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionMaster {
    @Id
    @Column(name = "icd9_code")
    private String icd9Code;

    @Column(name = "short_title")
    private String shortTitle;
    @Column(name = "long_title")
    private String longTitle;
    @Column(name="category")
    private String category;
    @Column(name="cat_code")
    private String catCode;
}
