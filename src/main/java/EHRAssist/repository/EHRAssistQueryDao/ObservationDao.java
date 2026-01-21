package EHRAssist.repository.EHRAssistQueryDao;

import EHRAssist.dto.ObservationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ObservationDao {
    List<Object[]> getAllObservation(ObservationDto request, Pageable pageable);

}
