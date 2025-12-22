package EHRAssist.repository;

import EHRAssist.model.PersonMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonMeasurementRepository extends JpaRepository<PersonMeasurement, Integer> {
    @Query(value = """
    WITH LatestMeasurements AS (
        SELECT
            pm.*,
            ROW_NUMBER() OVER (
                PARTITION BY pm.itemid
                ORDER BY pm.charttime DESC
            ) AS rn
        FROM UCIH.dbo.Person_Measurement pm
        WHERE pm.subject_id = :subjectId
          AND (:encounter IS NULL OR pm.hadm_id = :encounter)
    )
    SELECT
        lm.row_id,
        lm.subject_id,
        lm.hadm_id,
        lm.itemid,
        lm.charttime,
        lm.value,
        lm.valuenum,
        lm.valueuom,
        lm.flag,
        mm.label,
        mm.category,
        mm.fluid,
        mm.loinc_code
    FROM LatestMeasurements lm
    LEFT JOIN UCIH.dbo.Measurement_master mm
           ON mm.itemid = lm.itemid
    WHERE lm.rn = 1
      AND (
            :loincCode IS NULL
            OR :loincCode = ''
            OR mm.loinc_code = :loincCode
          )
""", nativeQuery = true)
    List<Object[]> findLatestMeasurements(
            @Param("subjectId") Integer subjectId,
            @Param("loincCode") String loincCode,
            @Param("encounter") Integer encounter
    );

}
