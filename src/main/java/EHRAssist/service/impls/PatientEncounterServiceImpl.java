package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientEncounterResponse;
import EHRAssist.dto.response.patientConditionResponse.Link;
import EHRAssist.dto.response.patientEncounterResponse.*;
import EHRAssist.model.VisitAdmissions;
import EHRAssist.repository.PatientEncounterRepository;
import EHRAssist.service.PatientEncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        response.setMeta(EntryMeta.builder()
                .lastUpdate("2025-12-16T07:04:34.392+00:00")
                .versionId("1").source("#nm0cjUfMVw6FekuB").build());
        response.setStatus("planned");
        response.setClasss(Classs.builder().code("AMB").display("ambulatory")
                .system("http://terminology.hl7.org/CodeSystem/v3-ActCode").build());
        response.setType(ResourceType.builder().text(List.of(admissions.getAdmissionType())).build());
        response.setSubject(Subject.builder().identifier("Patient/" + admissions.getSubjectId()).build());
        response.setPeriod(Period.builder().start(admissions.getAdmitTime().toString()).build());
        response.setExtension(List.of(Extension.builder()
                        .url("admission location")
                        .extension(List.of(EncounterMeta.builder()
                                .url("admission location")
                                .valueString(admissions.getAdmissionLocation()).build())).build(),
                Extension.builder()
                        .url("discharged location")
                        .extension(List.of(EncounterMeta.builder()
                                .url("discharged location")
                                .valueString(admissions.getDischargeLocation()).build())).build(),
                Extension.builder()
                        .url("insurance")
                        .extension(List.of(EncounterMeta.builder()
                                .url("insurance")
                                .valueString(admissions.getInsurance()).build())).build()));
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
                entry.setFullUrl("10.131.58.59:481/baseR4/Encounter/" + subjectId);
                entry.setResource(resourceMapper(ittr));
                entry.setSearch(Search.builder().mode("matched").build());
                return entry;
            }).toList();
            response.setTotal(entries.size());
            response.setEntry(entries);
        }
        response.setMeta(Meta.builder().lastUpdated("2025-12-25T08:45:29.650+00:00").build());
        response.setLink(List.of(Link.builder().relation("self")
                .url("10.131.58.59:481/baseR4/Encounter?_count=" + count + "&subject=" + subjectId).build()));
        response.setResourceType("Bundle");
        response.setType("searchset");
        return response;
    }
}
