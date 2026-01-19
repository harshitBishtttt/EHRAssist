package EHRAssist.repository.EHRAssistQueryDao;

import EHRAssist.dto.ObservationDto;

import java.util.List;

public interface ObservationDao {
    List<Object[]> setValueToNativeObservationQuery(ObservationDto request);

}
