package EHRAssist.service;

import EHRAssist.dto.request.ObservationRequest;
import EHRAssist.dto.request.observationMetaRequest.Code;
import EHRAssist.dto.request.observationMetaRequest.Specimen;
import EHRAssist.dto.request.observationMetaRequest.Subject;
import EHRAssist.dto.request.observationMetaRequest.ValueQuantity;
import EHRAssist.dto.response.ObservationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ObservationService {

    private void appendBasicClause(StringBuilder query, String entity, String attribute) {
        query.append("(").append(entity).append(".").append(attribute).append(" IS NULL OR ").append(entity).append(".")
                .append(attribute).append(" = :").append(attribute).append(") and ");
    }

    private void appendWithInClause(StringBuilder query, String entity, String attribute) {
        query.append(entity)
                .append(".")
                .append(attribute)
                .append(" IN (:")
                .append(attribute)
                .append(") and ");
    }

    private void appendOperatorBaseClause(StringBuilder query, String entity, String attribute, String operator) {
        query.append(entity)
                .append(".")
                .append(attribute)
                .append(" ")
                .append(operator)
                .append(" :")
                .append(attribute)
                .append(" and ");
    }

    private String buildObservationQuery(ObservationRequest request) {
        StringBuilder query = new StringBuilder("SELECT pm FROM PersonMeasurement pm " +
                "LEFT JOIN FETCH pm.measurementMasters pt where ");
        Code code = request.getCode();
        Subject subject = request.getSubject();
        List<String> itemId = request.getItemId();
        LocalDateTime effectiveDateTime = request.getEffectiveDateTime();
        Specimen specimen = request.getSpecimen();
        ValueQuantity valueQuantity = request.getValueQuantity();
        if (subject.getIdentifier() != null && !subject.getIdentifier().isEmpty()) {
            appendBasicClause(query, "pm", "subjectId");
        }
        if (effectiveDateTime != null) {
            appendBasicClause(query, "pm", "chartTime");
        }
        if (itemId != null && !itemId.isEmpty()) {
            appendWithInClause(query, "pm", "itemid");
        }
        if (specimen.getIdentifier() != null && !specimen.getIdentifier().isEmpty()) {
            appendBasicClause(query, "pm", "category");
        }
        if (code.getCoding() != null && !code.getCoding().isEmpty()) {
            appendBasicClause(query, "pm", "loincCode");
        }
        if (valueQuantity.getUnit() != null && !valueQuantity.getUnit().isEmpty()) {
            appendBasicClause(query, "pm", "valueUom");
        }
        if ((valueQuantity.getValue() != null && valueQuantity.getComparator() != null)
                && !valueQuantity.getComparator().isEmpty()) {
            appendOperatorBaseClause(query, "pm", "valueNum", valueQuantity.getComparator());
        }
        int len = query.length();
        if (len >= 4 && query.substring(len - 4).equals("and ")) {
            query.delete(len - 4, len);
        }
        return query.toString();

    }

    public ObservationResponse getPersonObservations(ObservationRequest request, Pageable page) {
        String query = buildObservationQuery(request);

        System.out.println(query);
        return null;
    }

}
