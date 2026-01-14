package EHRAssist.repository;

import EHRAssist.model.PersonProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonProcedureRepository extends JpaRepository<PersonProcedure, Integer> {
    @Query(value = "SELECT pp.*, pm.* " +
            "FROM UCIH.dbo.Person_Procedure pp " +
            "JOIN UCIH.dbo.Procedure_Master pm " +
            "ON pp.cpt_number BETWEEN pm.mincodeinsubsection AND pm.maxcodeinsubsection " +
            "WHERE (:subjectId IS NULL OR pp.subject_id = :subjectId) " +
            "AND (:encounterId IS NULL OR pp.row_id = :encounterId) " +
            "AND (:cptNumber IS NULL OR (pm.mincodeinsubsection <= :cptNumber AND pm.maxcodeinsubsection >= :cptNumber))",
            nativeQuery = true)
    List<Object[]> searchPersonProcedure(
            @Param("subjectId") Integer subjectId,
            @Param("hadmId") Integer encounterId,
            @Param("cptNumber") Integer cptNumber
    );

}
