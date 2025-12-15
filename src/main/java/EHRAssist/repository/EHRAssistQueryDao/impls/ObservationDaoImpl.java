package EHRAssist.repository.EHRAssistQueryDao.impls;

import EHRAssist.dto.request.ObservationRequest;
import EHRAssist.dto.request.observationMetaRequest.Code;
import EHRAssist.dto.request.observationMetaRequest.Specimen;
import EHRAssist.dto.request.observationMetaRequest.Subject;
import EHRAssist.dto.request.observationMetaRequest.ValueQuantity;
import EHRAssist.repository.EHRAssistQueryDao.ObservationDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ObservationDaoImpl implements ObservationDao {


    @PersistenceContext
    private EntityManager entityManager;

    public String getNativeObservationQuery(ObservationRequest request) {
        StringBuilder query = new StringBuilder(
                "WITH LatestMeasurements AS ( " +
                        "    SELECT pm.*, ROW_NUMBER() OVER ( " +
                        "        PARTITION BY pm.itemid " +
                        "        ORDER BY pm.charttime DESC " +
                        "    ) AS rn " +
                        "    FROM UCIH.dbo.Person_Measurement pm " +
                        "    WHERE pm.subject_id = :subjectId " +
                        ") " +
                        "SELECT " +
                        "    lm.row_id ,lm.subject_id, " +
                        "    lm.hadm_id, " +
                        "    lm.itemid, " +
                        "    lm.charttime, " +
                        "    lm.value, " +
                        "    lm.valuenum, " +
                        "    lm.valueuom, " +
                        "    lm.flag, " +
                        "    mm.label, " +
                        "    mm.category, " +
                        "    mm.fluid, mm.category, " +
                        "    mm.loinc_code " +
                        "FROM LatestMeasurements lm " +
                        "LEFT JOIN UCIH.dbo.Measurement_master mm ON mm.itemid = lm.itemid " +
                        "WHERE lm.rn = 1 "
        );

        if (request.getItemId() != null && !request.getItemId().isEmpty()) {
            query.append(" AND mm.itemid IN (:itemId) ");
        }

        if (request.getValueQuantity() != null && request.getValueQuantity().getUnit() != null &&
                !request.getValueQuantity().getUnit().isEmpty()) {
            query.append(" AND lm.valueuom = :valueUom ");
        }

        if (request.getValueQuantity() != null && request.getValueQuantity().getValue() != null &&
                request.getValueQuantity().getComparator() != null &&
                !request.getValueQuantity().getComparator().isEmpty()) {
            query.append(" AND lm.valuenum ").append(request.getValueQuantity().getComparator()).append(" :valueNum");
        }
        if (request.getSpecimen() != null && request.getSpecimen().getIdentifier() != null &&
                !request.getSpecimen().getIdentifier().isEmpty()) {
            query.append(" AND mm.fluid = :fluid ");
        }
        if (request.getCode() != null && request.getCode().getCoding() != null &&
                !request.getCode().getCoding().isEmpty()) {
            query.append(" AND mm.loinc_code = :loincCode ");
        }

        return query.toString();
    }


    public Query setValueToNativeObservationQuery(String sql, ObservationRequest request) {
        Query query = entityManager.createNativeQuery(sql);

        Subject subject = request.getSubject();
        LocalDateTime effectiveDateTime = request.getEffectiveDateTime();
        List<String> itemId = request.getItemId();
        Specimen specimen = request.getSpecimen();
        Code code = request.getCode();
        ValueQuantity valueQuantity = request.getValueQuantity();

        if (subject.getIdentifier() != null && !subject.getIdentifier().isEmpty()) {
            query.setParameter("subjectId", subject.getIdentifier());
        }

        if (effectiveDateTime != null) {
            query.setParameter("chartTime", effectiveDateTime);
        }

        if (itemId != null && !itemId.isEmpty()) {
            query.setParameter("itemId", itemId);
        }

        if (specimen.getIdentifier() != null && !specimen.getIdentifier().isEmpty()) {
            query.setParameter("fluid", specimen.getIdentifier());
        }

        if (code.getCoding() != null && !code.getCoding().isEmpty()) {
            query.setParameter("loincCode", code.getCoding());
        }

        if (valueQuantity.getUnit() != null && !valueQuantity.getUnit().isEmpty()) {
            query.setParameter("valueUom", valueQuantity.getUnit());
        }

        if (valueQuantity.getValue() != null) {
            query.setParameter("valueNum", valueQuantity.getValue());
        }

        return query;
    }
}
