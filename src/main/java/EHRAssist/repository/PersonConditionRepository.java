package EHRAssist.repository;

import EHRAssist.model.PersonCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonConditionRepository extends JpaRepository<PersonCondition, Integer> {

    @Query("""
                SELECT pc
                FROM PersonCondition pc
                WHERE (:subject IS NULL OR pc.person.subjectId = :subject)
                  AND (:code IS NULL OR :code = '' OR pc.conditionMaster.icd9Code = :code)
                  AND (:encounter IS NULL OR pc.rowId = :encounter)
            """)
    Optional<List<PersonCondition>> searchPersonCondition(
            @Param("subject") Integer subject,
            @Param("code") String code,
            @Param("encounter") Integer encounter
    );


}
