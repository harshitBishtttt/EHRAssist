package EHRAssist.repository;

import EHRAssist.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Short> {
    @Query("""
                SELECT DISTINCT p
                FROM Person p
                LEFT JOIN p.personName pn
                LEFT JOIN p.personTelecom pt
                WHERE (:family IS NULL OR :family = '' OR pn.lastName = :family)
                  AND (:given IS NULL OR :given = '' OR pn.firstName = :given)
                  AND (:gender IS NULL OR :gender = '' OR p.gender = :gender)
                  AND (:birthdate IS NULL OR p.birthdate = :birthdate)
                  AND (
                        :email IS NULL OR :email = '' 
                        OR (pt.system = 'email' AND pt.value = :email)
                      )
                  And (
                        :phone IS NULL OR :phone = '' 
                        OR (pt.system = 'phone' AND pt.value = :phone)
                      )
            """)
    Optional<List<Person>> searchPerson(
            @Param("family") String family,
            @Param("given") String given,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("birthdate") LocalDate birthdate,
            @Param("gender") String gender
    );


    Optional<Person> findByPersonTelecom_SystemAndPersonTelecom_Value(String system, String value);


}
