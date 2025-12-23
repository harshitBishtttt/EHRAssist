package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientEncounterResponse;
import EHRAssist.dto.response.patientEncounterResponse.*;
import EHRAssist.dto.response.patientEncounterResponse.Search;
import EHRAssist.model.VisitAdmissions;
import EHRAssist.repository.PatientEncounterRepository;
import EHRAssist.service.PatientEncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PatientEncounterServiceImpl implements PatientEncounterService {

    @Autowired
    private PatientEncounterRepository patientEncounterRepository;

    private Resource resourceMapper(VisitAdmissions admissions) {
        Resource response = new Resource();
        response.setResourceType("Encounter");
        response.setId(admissions.getRowId().toString());
        response.setMeta(EntryMeta.builder().build());
        response.setType(ResourceType.builder().text(List.of(admissions.getAdmissionType())).build());
        response.setSubject(Subject.builder().identifier("Patient/" + admissions.getSubjectId()).build());
        response.setPeriod(Period.builder().start(admissions.getAdmitTime().toString()).build());
        response.setExtension(Arrays.asList(Extension.builder()
                        .url("admission location")
                        .valueString(admissions.getAdmissionLocation()).build(),
                Extension.builder().url("discharged location").valueString(admissions.getDischargeLocation()).build(),
                Extension.builder().url("insurance").valueString(admissions.getEthnicity()).build()));
        response.setClass_(Classs.builder().build());
        return response;
    }

    public PatientEncounterResponse getPatientEncounter(Integer subjectId, Integer count,
                                                        Pageable pageable) {
        PatientEncounterResponse response = new PatientEncounterResponse();
        Optional<List<VisitAdmissions>> bySubjectId = patientEncounterRepository.findBySubjectId(subjectId);
        if (bySubjectId.isPresent()) {
            response.setId(subjectId.toString());
            List<VisitAdmissions> visitAdmissions = bySubjectId.get();
            List<Entry> entries = visitAdmissions.stream().map(ittr -> {
                Entry entry = new Entry();
                entry.setFullUrl("");
                entry.setResource(resourceMapper(ittr));
                entry.setSearch(Search.builder().mode("matched").build());
                return entry;
            }).toList();
            response.setTotal(entries.size());
            response.setEntry(entries);
        }
        response.setLink(null);
        response.setResourceType("Bundle");
        response.setType("searchset");
        return response;
    }
}
