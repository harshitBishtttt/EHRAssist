package EHRAssist.service.impls;

import EHRAssist.model.VisitAdmissions;
import EHRAssist.repository.PatientEncounterRepository;
import EHRAssist.service.PatientEncounterService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientEncounterServiceImpl implements PatientEncounterService {

    @Autowired
    private PatientEncounterRepository patientEncounterRepository;

    private Encounter resourceMapper(VisitAdmissions admissions) {

        Encounter encounter = new Encounter();
        encounter.setId(admissions.getRowId().toString());
        Meta meta = new Meta();
        meta.setVersionId("2");
        meta.setLastUpdatedElement(new InstantType("2025-12-16T07:04:34.392+00:00"));
        meta.setSource("#nm0cjUfMVw6FekuB");
        encounter.setMeta(meta);
        encounter.setStatus(Encounter.EncounterStatus.PLANNED);
        Coding classCoding = new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode")
                .setCode("AMB")
                .setDisplay("ambulatory");

        encounter.setClass_(classCoding);
        CodeableConcept typeConcept = new CodeableConcept();
        typeConcept.setText(admissions.getAdmissionType());
        encounter.addType(typeConcept);
        Reference subjectRef = new Reference();
        subjectRef.setReference("Patient/" + admissions.getSubjectId());
        encounter.setSubject(subjectRef);

        LocalDateTime localDateTime = admissions.getAdmitTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());

        Period period = new Period();
        period.setStartElement(new DateTimeType(date));
        encounter.setPeriod(period);
        Extension admissionExt = new Extension();
        admissionExt.setUrl("admission location");
        admissionExt.addExtension(
                new Extension("admission location",
                        new StringType(admissions.getAdmissionLocation()))
        );
        Extension dischargeExt = new Extension();
        dischargeExt.setUrl("discharged location");
        dischargeExt.addExtension(
                new Extension("discharged location",
                        new StringType(admissions.getDischargeLocation()))
        );
        Extension insuranceExt = new Extension();
        insuranceExt.setUrl("insurance");
        insuranceExt.addExtension(
                new Extension("insurance",
                        new StringType(admissions.getInsurance()))
        );
        Attachment attachment1 = new Attachment();
        attachment1.setContentType("application/pdf");
        attachment1.setTitle("Example PDF");
        attachment1.setUrl("https://example.com/example.pdf");

        Extension attachmentExt1 = new Extension();
        attachmentExt1.setUrl("attachedDocument");
        attachmentExt1.setValue(attachment1);

        Attachment attachment2 = new Attachment();
        attachment2.setContentType("application/pdf");
        attachment2.setTitle("Test PDF");
        attachment2.setUrl("https://example.com/test.pdf");

        Extension attachmentExt2 = new Extension();
        attachmentExt2.setUrl("attachedDocument");
        attachmentExt2.setValue(attachment2);

        encounter.addExtension(admissionExt);
        encounter.addExtension(dischargeExt);
        encounter.addExtension(insuranceExt);
        encounter.addExtension(attachmentExt1);
        encounter.addExtension(attachmentExt2);

        return encounter;
    }

    public Bundle getPatientEncounter(Integer subjectId, Integer count, Pageable pageable) {

        Bundle bundle = new Bundle();

        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setId(subjectId.toString());

        Meta meta = new Meta();
        meta.setLastUpdatedElement(new InstantType("2025-12-25T08:45:29.650+00:00"));
        bundle.setMeta(meta);

        Optional<List<VisitAdmissions>> bySubjectId =
                patientEncounterRepository.findBySubjectId(subjectId);

        if (bySubjectId.isPresent()) {

            List<VisitAdmissions> visitAdmissions = bySubjectId.get();

            for (VisitAdmissions visit : visitAdmissions) {

                Encounter encounter = resourceMapper(visit);

                Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
                entry.setFullUrl("10.131.58.59:481/baseR4/Encounter/" + subjectId);
                entry.setResource(encounter);

                Bundle.BundleEntrySearchComponent search =
                        new Bundle.BundleEntrySearchComponent();
                search.setMode(Bundle.SearchEntryMode.MATCH);
                entry.setSearch(search);

                bundle.addEntry(entry);
            }

            bundle.setTotal(visitAdmissions.size());
        }

        Bundle.BundleLinkComponent link = new Bundle.BundleLinkComponent();
        link.setRelation("self");
        link.setUrl("10.131.58.59:481/baseR4/Encounter?_count=" + count + "&subject=" + subjectId);
        bundle.addLink(link);

        return bundle;
    }

    //old snippet
    //    private Resource resourceMapper(VisitAdmissions admissions) {
//        Resource response = new Resource();
//        response.setResourceType("Encounter");
//        response.setId(admissions.getRowId().toString());
//        response.setMeta(EntryMeta.builder().build());
//        response.setMeta(EntryMeta.builder()
//                .lastUpdate("2025-12-16T07:04:34.392+00:00")
//                .versionId("2").source("#nm0cjUfMVw6FekuB").build());
//        response.setStatus("planned");
//        response.setClasss(Classs.builder().code("AMB").display("ambulatory")
//                .system("http://terminology.hl7.org/CodeSystem/v3-ActCode").build());
//        response.setType(List.of(ResourceType.builder().text(admissions.getAdmissionType()).build()));
//        response.setSubject(Subject.builder().reference("Patient/" + admissions.getSubjectId()).build());
//        response.setPeriod(Period.builder().start(admissions.getAdmitTime().toString()).build());
//        response.setExtension(List.of(ExtensionTypeOne.builder()
//                        .url("admission location")
//                        .extension(List.of(EncounterMeta.builder()
//                                .url("admission location")
//                                .valueString(admissions.getAdmissionLocation()).build())).build(),
//                ExtensionTypeOne.builder()
//                        .url("discharged location")
//                        .extension(List.of(EncounterMeta.builder()
//                                .url("discharged location")
//                                .valueString(admissions.getDischargeLocation()).build())).build(),
//                ExtensionTypeOne.builder()
//                        .url("insurance")
//                        .extension(List.of(EncounterMeta.builder()
//                                .url("insurance")
//                                .valueString(admissions.getInsurance()).build())).build(),
//                ExtensionTypeTwo.builder()
//                        .url("attachedDocument")
//                        .valueAttachment(ValueAttachment.builder()
//                                .contentType("application/pdf")
//                                .title("Example PDF")
//                                .url("https://example.com/example.pdf").build()).build(),
//                ExtensionTypeTwo.builder()
//                        .url("attachedDocument")
//                        .valueAttachment(ValueAttachment.builder()
//                                .contentType("application/pdf")
//                                .title("Test PDF")
//                                .url("https://example.com/test.pdf").build()).build()));
//        return response;
//    }
//
//    public PatientEncounterResponse getPatientEncounter(Integer subjectId, Integer count,
//                                                        Pageable pageable) {
//        PatientEncounterResponse response = new PatientEncounterResponse();
//        Optional<List<VisitAdmissions>> bySubjectId = patientEncounterRepository.findBySubjectId(subjectId);
//        if (bySubjectId.isPresent()) {
//            response.setId(subjectId.toString());
//            List<VisitAdmissions> visitAdmissions = bySubjectId.get();
//            List<Entry> entries = visitAdmissions.stream().map(ittr -> {
//                Entry entry = new Entry();
//                entry.setFullUrl("10.131.58.59:481/baseR4/Encounter/" + subjectId);
//                entry.setResource(resourceMapper(ittr));
//                entry.setSearch(Search.builder().mode("matched").build());
//                return entry;
//            }).toList();
//            response.setTotal(entries.size());
//            response.setEntry(entries);
//        }
//        response.setMeta(Meta.builder().lastUpdated("2025-12-25T08:45:29.650+00:00").build());
//        response.setLink(List.of(Link.builder().relation("self")
//                .url("10.131.58.59:481/baseR4/Encounter?_count=" + count + "&subject=" + subjectId).build()));
//        response.setResourceType("Bundle");
//        response.setType("searchset");
//        return response;
//  }
}
