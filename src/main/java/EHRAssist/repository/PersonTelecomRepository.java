package EHRAssist.repository;

import EHRAssist.model.PersonTelecom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTelecomRepository extends JpaRepository<PersonTelecom, Integer> {
}
