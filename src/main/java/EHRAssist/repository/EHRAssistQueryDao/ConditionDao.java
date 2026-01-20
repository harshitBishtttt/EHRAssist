package EHRAssist.repository.EHRAssistQueryDao;

import java.util.List;

public interface ConditionDao {
    List<Object[]> getMyConditions(Integer subjectId, String code, Integer encounter);
}
