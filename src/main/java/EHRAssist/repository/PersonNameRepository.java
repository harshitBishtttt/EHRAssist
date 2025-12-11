package EHRAssist.repository;

import EHRAssist.model.PersonName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonNameRepository extends JpaRepository<PersonName, Integer> {
}
