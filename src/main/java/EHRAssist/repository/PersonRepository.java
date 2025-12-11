package EHRAssist.repository;

import EHRAssist.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Short> {
    @Query("""
    SELECT DISTINCT p FROM Person p
        JOIN p.personAddress pa
        JOIN p.personName pn
        JOIN p.patientTelecom pt
    WHERE (:gender IS NULL OR :gender = '' OR p.gender = :gender)
        AND (:birthdate IS NULL OR p.birthdate = :birthdate)
        AND (:family IS NULL OR :family = '' OR pn.family = :family)
        AND (:given IS NULL OR :given = '' OR pn.given = :given)
        AND (:name IS NULL OR :name = '' OR pn.name = :name)
        AND (:city IS NULL OR :city = '' OR pa.city = :city)
        AND (:state IS NULL OR :state = '' OR pa.state = :state)
        AND (:postalCode IS NULL OR :postalCode = '' OR pa.postalCode = :postalCode)
        AND (:line IS NULL OR :line = '' OR pa.line = :line)
        AND (:system IS NULL OR :system = '' OR pt.system = :system)
        AND (:value IS NULL OR :value = '' OR pt.value = :value)
""")
    List<Person> findAllPersons(
            @Param("gender") String gender,
            @Param("birthdate") LocalDate birthdate,
            @Param("family") String family,
            @Param("given") String given,
            @Param("name") String name,
            @Param("city") String city,
            @Param("state") String state,
            @Param("postalCode") String postalCode,
            @Param("line") String line,
            @Param("system") String system,
            @Param("value") String value
    );

}
