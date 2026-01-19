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
                    WHERE (:subjectId IS NULL OR pm.subject_id = :subjectId)
                      AND (:encounter IS NULL OR pm.row_id = :encounter)
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
                   And (:valueNum Is Null or :valueNum = '' or lm.valuenum = :valueNum)
                   And (:uom Is Null or :uom = '' or lm.valueuom = :uom)
                   AND (:loincCode IS NULL OR :loincCode = '' OR mm.loinc_code = :loincCode)
            """, nativeQuery = true)
    List<Object[]> findLatestMeasurementsOld(
            @Param("subjectId") Integer subjectId,
            @Param("encounter") Integer encounter,
            @Param("valueNum") Double valueNum,
            @Param("uom") String uom,
            @Param("loincCode") String loincCode
    );

    @Query(value = """
               SELECT
               pm.row_id,
               pm.subject_id,
               pm.hadm_id,
               pm.itemid,
               pm.charttime,
               COALESCE(pm.value, '')        AS value,
               pm.valuenum,
               COALESCE(pm.valueuom, '')     AS valueuom,
               COALESCE(pm.flag, '')         AS flag,
               COALESCE(mm.label, '')        AS label,
               COALESCE(mm.category, '')     AS category,
               COALESCE(mm.fluid, '')        AS fluid,
               COALESCE(mm.loinc_code, '')   AS loinc_code,
               COALESCE(pn.first_name, '')   AS first_name,
               COALESCE(pn.middle_name, '')  AS middle_name,
               COALESCE(pn.last_name, '')    AS last_name
           FROM UCIH.dbo.Person_Measurement pm
           LEFT JOIN UCIH.dbo.Measurement_master mm
                  ON mm.itemid = pm.itemid
           LEFT JOIN UCIH.dbo.Person_Name pn
                  ON pn.subject_id_from_person_table = pm.subject_id
                 AND pn.name_type = 'official'
           WHERE
               (:subjectId IS NULL OR pm.subject_id = :subjectId)
           AND (:encounter IS NULL OR pm.row_id = :encounter)
           AND (:valueNum IS NULL OR pm.valuenum = :valueNum)
           AND (:uom IS NULL OR :uom = '' OR pm.valueuom = :uom)
           AND (:loincCode IS NULL OR mm.loinc_code = :loincCode);
           """, nativeQuery = true)
    List<Object[]> findMeasurements(
            @Param("subjectId") Integer subjectId,
            @Param("encounter") Integer encounter,
            @Param("valueNum") Double valueNum,
            @Param("uom") String uom,
            @Param("loincCode") String loincCode
    );


}
