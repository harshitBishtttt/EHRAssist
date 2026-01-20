package EHRAssist.repository.EHRAssistQueryDao.impls;

import EHRAssist.dto.FhirQuantity;
import EHRAssist.dto.ObservationDto;
import EHRAssist.repository.EHRAssistQueryDao.ObservationDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class ObservationDaoImpl implements ObservationDao {

    @PersistenceContext
    private EntityManager entityManager;

    private String getNativeObservationQuery(ObservationDto request) {

        StringBuilder query = new StringBuilder("""
                    WITH LatestMeasurements AS (
                        SELECT
                             pm.*,
                             ROW_NUMBER() OVER (
                                 PARTITION BY pm.subject_id, pm.itemid
                                 ORDER BY pm.charttime DESC
                             ) AS rn
                         FROM
                             UCIH.dbo.Person_Measurement pm
                     WHERE
                        1 = 1
                """);

        if (!ObjectUtils.isEmpty(request.getSubject())) {
            query.append(" AND pm.subject_id = :subjectId ");
        }

        if (!ObjectUtils.isEmpty(request.getEncounter())) {
            query.append(" AND pm.row_id = :encounter ");
        }

        query.append("""
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
                """);

        if (!ObjectUtils.isEmpty(request.getCode()) && !ObjectUtils.isEmpty(request.getFhirQuantity())) {
            FhirQuantity fhirQuantity = request.getFhirQuantity();

            switch (fhirQuantity.getPrefix().toLowerCase()) {
                case "eq" -> query.append(" AND lm.valuenum = :valueNum ");
                case "gt" -> query.append(" AND lm.valuenum > :valueNum ");
                case "lt" -> query.append(" AND lm.valuenum < :valueNum ");
                case "ge" -> query.append(" AND lm.valuenum >= :valueNum ");
                case "le" -> query.append(" AND lm.valuenum <= :valueNum ");
                case "ne" -> query.append(" AND lm.valuenum <> :valueNum ");
            }

            query.append(" AND lm.valueuom = :uom ");
        }

        if (!ObjectUtils.isEmpty(request.getCode())) {
            query.append(" AND mm.loinc_code = :loincCode ");
        }

        return query.toString();
    }

    public List<Object[]> getAllObservation(ObservationDto request) {

        String sql = getNativeObservationQuery(request);
        Query query = entityManager.createNativeQuery(sql);

        if (!ObjectUtils.isEmpty(request.getSubject())) {
            query.setParameter("subjectId", request.getSubject());
        }

        if (!ObjectUtils.isEmpty(request.getEncounter())) {
            query.setParameter("encounter", request.getEncounter());
        }

        if (!ObjectUtils.isEmpty(request.getCode()) && !ObjectUtils.isEmpty(request.getFhirQuantity())) {
            FhirQuantity fhirQuantity = request.getFhirQuantity();

            if (!ObjectUtils.isEmpty(fhirQuantity.getNumber())) {
                query.setParameter("valueNum", fhirQuantity.getNumber());
            }

            if (!ObjectUtils.isEmpty(fhirQuantity.getUnit())) {
                query.setParameter("uom", fhirQuantity.getUnit());
            }
        }

        if (!ObjectUtils.isEmpty(request.getCode())) {
            query.setParameter("loincCode", request.getCode());
        }

        return query.getResultList();
    }

}
