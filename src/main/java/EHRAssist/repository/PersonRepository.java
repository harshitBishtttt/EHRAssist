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
                    LEFT JOIN  p.personAddress pa
                    LEFT JOIN  p.personName pn
                    LEFT JOIN  p.patientTelecom pt
                WHERE (:gender IS NULL OR :gender = '' OR p.gender = :gender)
                    AND (:birthdate IS NULL OR p.birthdate = :birthdate)
                    AND (:firstName IS NULL OR :firstName = '' OR pn.firstName = :firstName)
                    AND (:middleName IS NULL OR :middleName = '' OR pn.middleName = :middleName)
                    AND (:lastName IS NULL OR :lastName = '' OR pn.lastName = :lastName)
                    AND (:addressOne IS NULL OR :addressOne = '' OR pa.addressOne = :addressOne)
                    AND (:addressTwo IS NULL OR :addressTwo = '' OR pa.addressTwo = :addressTwo)
                    AND (:addressThree IS NULL OR :addressThree = '' OR pa.addressThree = :addressThree)
                    AND (:postalCode IS NULL OR :postalCode = '' OR pa.postalCode = :postalCode)
                    AND (:country IS NULL OR :country = '' OR pa.country = :country)
                    AND (:system IS NULL OR :system = '' OR pt.system = :system)
                    AND (:value IS NULL OR :value = '' OR pt.value = :value)
                    AND (:useTel IS NULL OR :useTel = '' OR pt.useTel = :useTel)
            """)
    List<Person> findAllPersons(
            @Param("gender") String gender,
            @Param("birthdate") LocalDate birthdate,
            @Param("firstName") String firstName,
            @Param("middleName") String middleName,
            @Param("lastName") String lastName,
            @Param("addressOne") String addressOne,
            @Param("addressTwo") String addressTwo,
            @Param("addressThree") String addressThree,
            @Param("postalCode") String postalCode,
            @Param("country") String country,
            @Param("system") String system,
            @Param("value") String value,
            @Param("useTel") String useTel
    );


}
