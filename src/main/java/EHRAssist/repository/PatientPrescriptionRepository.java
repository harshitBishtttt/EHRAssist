package EHRAssist.repository;

import EHRAssist.model.PersonPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientPrescriptionRepository extends JpaRepository<PersonPrescription, Integer> {
    @Query("""
                SELECT pm
                FROM PersonPrescription pm
                WHERE (:subjectId IS NULL OR pm.subjectId = :subjectId)
                  AND (:rowId IS NULL OR pm.rowId = :rowId)
            """)
    List<PersonPrescription> findBySubjectIdOrRowId(
            @Param("subjectId") Integer subjectId,
            @Param("rowId") Integer rowId
    );

}
