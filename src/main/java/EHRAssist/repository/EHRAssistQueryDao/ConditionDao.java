package EHRAssist.repository.EHRAssistQueryDao;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConditionDao {
    List<Object[]> getMyConditions(Integer subjectId, String code, Integer encounter, Pageable pageable);
}
