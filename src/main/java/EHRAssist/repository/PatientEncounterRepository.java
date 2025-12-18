package EHRAssist.repository;

import EHRAssist.model.VisitAdmissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientEncounterRepository extends JpaRepository<VisitAdmissions, Integer> {
    Optional<List<VisitAdmissions>> findBySubjectId(Integer subjectId);
}
