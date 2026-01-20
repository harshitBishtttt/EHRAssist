package EHRAssist.repository.EHRAssistQueryDao.impls;

import EHRAssist.repository.EHRAssistQueryDao.ConditionDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class ConditionDaoImpl implements ConditionDao {
    @PersistenceContext
    private EntityManager entityManager;

    private String getNativeObservationQuery(Integer subjectId, String code, Integer encounter) {
        String query = """
                SELECT
                	 pc.*,p.*,cm.*
                FROM
                	UCIH.dbo.Person_Condition pc
                left JOIN UCIH.dbo.Condition_Master cm\s
                    ON
                	pc.icd9_code = cm.icd9_code
                left JOIN UCIH.dbo.Person_Name p\s
                    ON
                	pc.subject_id = p.subject_id_from_person_table and p.name_type = 'official'
                WHERE 1=1""";
        if (!ObjectUtils.isEmpty(subjectId)) {
            query += " AND  pc.subject_Id  = :subjectId ";
        }
        if (!ObjectUtils.isEmpty(code)) {
            query += " AND cm.icd9_code = :code ";
        }
        if (!ObjectUtils.isEmpty(encounter)) {
            query += " AND pc.row_id = :encounter ;";
        }
        return query;
    }

    @Override
    public List<Object[]> getMyConditions(Integer subjectId, String code, Integer encounter) {

        String sql = getNativeObservationQuery(subjectId, code, encounter);
        Query query = entityManager.createNativeQuery(sql);

        if (!ObjectUtils.isEmpty(subjectId)) {
            query.setParameter("subjectId", subjectId);
        }
        if (!ObjectUtils.isEmpty(code)) {
            query.setParameter("code", code);
        }
        if (!ObjectUtils.isEmpty(encounter)) {
            query.setParameter("encounter", encounter);
        }
        return query.getResultList();
    }

}
