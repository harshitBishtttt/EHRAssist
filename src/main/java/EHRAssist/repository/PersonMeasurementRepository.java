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
        COALESCE(lm.value, '')        AS value,
        lm.valuenum,
        COALESCE(lm.valueuom, '')     AS valueuom,
        COALESCE(lm.flag, '')         AS flag,
        COALESCE(mm.label, '')        AS label,
        COALESCE(mm.category, '')     AS category,
        COALESCE(mm.fluid, '')        AS fluid,
        COALESCE(mm.loinc_code, '')   AS loinc_code,
        COALESCE(pn.first_name, '')   AS first_name,
        COALESCE(pn.middle_name, '')  AS middle_name,
        COALESCE(pn.last_name, '')    AS last_name
    FROM LatestMeasurements lm
    LEFT JOIN UCIH.dbo.Measurement_master mm
           ON mm.itemid = lm.itemid
    LEFT JOIN UCIH.dbo.Person_Name pn
           ON pn.subject_id_from_person_table = lm.subject_id
          AND pn.name_type = 'official'
    WHERE lm.rn = 1
      AND (
            :loincCode IS NULL
            OR :loincCode = ''
            OR mm.loinc_code = :loincCode
          ) And lm.valuenum != 0
""", nativeQuery = true)
    List<Object[]> findLatestMeasurements(
            @Param("subjectId") Integer subjectId,
            @Param("loincCode") String loincCode,
            @Param("encounter") Integer encounter
    );


}
