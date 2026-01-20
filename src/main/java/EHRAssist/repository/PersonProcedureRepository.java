package EHRAssist.repository;

import EHRAssist.model.PersonProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonProcedureRepository extends JpaRepository<PersonProcedure, Integer> {

    @Query(value = """
            SELECT
                pp.*,
                pm.*,
                pn.first_name,
                pn.middle_name,
                pn.last_name
            FROM UCIH.dbo.Person_Procedure pp
            JOIN UCIH.dbo.Procedure_Master pm
                ON pp.cpt_number BETWEEN pm.mincodeinsubsection AND pm.maxcodeinsubsection
            LEFT JOIN UCIH.dbo.Person_Name pn
                ON pn.subject_id_from_person_table = pp.subject_id
                AND pn.name_type = 'official'
            WHERE (:subjectId IS NULL OR pp.subject_id = :subjectId)
              AND (:encounterId IS NULL OR pp.row_id = :encounterId)
              AND (
                    :cptNumber IS NULL
                    OR (pm.mincodeinsubsection <= :cptNumber AND pm.maxcodeinsubsection >= :cptNumber)
                  );
            """,
            nativeQuery = true)
    List<Object[]> searchPersonProcedure(
            Integer subjectId,
            Integer encounterId,
            Integer cptNumber
    );

}
