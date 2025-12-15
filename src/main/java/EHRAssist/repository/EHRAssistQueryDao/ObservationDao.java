package EHRAssist.repository.EHRAssistQueryDao;

import EHRAssist.dto.request.ObservationRequest;
import jakarta.persistence.Query;

public interface ObservationDao {
    String getNativeObservationQuery(ObservationRequest request);

    Query setValueToNativeObservationQuery(String sql, ObservationRequest request);

}
