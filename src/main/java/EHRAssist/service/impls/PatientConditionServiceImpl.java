package EHRAssist.service.impls;

import EHRAssist.exceptionHandler.exceptions.FhirBadRequestException;
import EHRAssist.repository.EHRAssistQueryDao.ConditionDao;
import EHRAssist.service.PatientConditionService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PatientConditionServiceImpl implements PatientConditionService {

//    @Autowired
//    private PersonConditionRepository personConditionRepository;

    @Autowired
    private ConditionDao conditionDao;

    public Bundle getPatientCondition(Integer subject,
                                      String code,
                                      Integer encounter,
                                      Pageable pageable) {

        Bundle bundle = new Bundle();

        if (ObjectUtils.isEmpty(subject) && ObjectUtils.isEmpty(code) && ObjectUtils.isEmpty(encounter)) {
            throw new FhirBadRequestException("In Condition search params are missing, at least provide code!");
        }

        List<Object[]> personConditions =
                conditionDao.getMyConditions(subject, code, encounter,pageable);

        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setId(UUID.randomUUID().toString());
        bundle.setTotal(personConditions.size());
        bundle.setMeta(new Meta().setLastUpdated(new Date()));
        bundle.addLink()
                .setRelation("self")
                .setUrl("10.131.58.59:481/baseR4/Condition?subject=" + subject + "&code=" + code);

        bundle.addLink()
                .setRelation("next")
                .setUrl("10.131.58.59:481/baseR4?_getpages=&_getpagesoffset=&_count=&_pretty=&_bundletype=");

        if (personConditions.isEmpty()) {
            return bundle;
        }

        for (Object[] pc : personConditions) {
            Condition condition = new Condition();
            condition.setId(!ObjectUtils.isEmpty(pc[0]) ? String.valueOf(pc[0]) : "");
            condition.setMeta(new Meta()
                    .setVersionId("1")
                    .setLastUpdated(new Date())
                    .setSource("#II7YGAcYPBuLlrB3"));
            condition.setClinicalStatus(new CodeableConcept().addCoding(
                    new Coding()
                            .setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")
                            .setCode("active")
                            .setDisplay("Active")
            ));
            condition.setVerificationStatus(new CodeableConcept().addCoding(
                    new Coding()
                            .setSystem("http://terminology.hl7.org/CodeSystem/condition-ver-status")
                            .setCode("confirmed")
                            .setDisplay("Confirmed")
            ));
            condition.addCategory(new CodeableConcept().addCoding(
                    new Coding()
                            .setSystem("http://terminology.hl7.org/CodeSystem/condition-category")
                            .setCode("problem-list-item")
                            .setDisplay("Problem List Item")
            ));
            condition.setSeverity(new CodeableConcept().addCoding(
                    new Coding()
                            .setSystem("http://snomed.info/sct")
                            .setCode(!ObjectUtils.isEmpty(pc[5]) ? String.valueOf(pc[5]) : "")
                            .setDisplay(!ObjectUtils.isEmpty(pc[6]) ? String.valueOf(pc[6]) : "")
            ));
            condition.setCode(new CodeableConcept()
                    .setText(!ObjectUtils.isEmpty(pc[16]) ? String.valueOf(pc[16]) : "")
                    .addCoding(new Coding()
                            .setSystem("http://hl7.org/fhir/sid/icd-10-cm")
                            .setCode(!ObjectUtils.isEmpty(pc[14]) ? String.valueOf(pc[14]).trim() : "")
                            .setDisplay(!ObjectUtils.isEmpty(pc[16]) ? String.valueOf(pc[16]) : "")
                    ));
            condition.setSubject(new Reference(!ObjectUtils.isEmpty(pc[8]) ? "Patient/" + String.valueOf(pc[8]) : "")
                    .setDisplay(!ObjectUtils.isEmpty(pc[9]) ? " " + (pc[9])
                            + (!ObjectUtils.isEmpty(pc[10]) ? " " + (pc[10])
                            + (!ObjectUtils.isEmpty(pc[11]) ? String.valueOf(" " + pc[11]) : "") : "") : ""));


            condition.setEncounter(new Reference(!ObjectUtils.isEmpty(pc[16]) ? "Encounter/" + (pc[0]) : ""));
            condition.setRecordedDate(new Date());
            Bundle.BundleEntryComponent entry = bundle.addEntry();
            entry.setFullUrl("10.131.58.59:481/baseR4/Condition?subject=&code=");
            entry.setResource(condition);
            entry.setSearch(new Bundle.BundleEntrySearchComponent()
                    .setMode(Bundle.SearchEntryMode.MATCH));
        }
        return bundle;
    }

    //old snippet
    //    public PatientConditionResponse getPatientConditionOld(Integer subject,
//                                                        String code,
//                                                        Integer encounter,
//                                                        Pageable pageable) {
//        PatientConditionResponse response = new PatientConditionResponse();
//        if ((subject == null) && ((code == null) || code.isEmpty()) && (encounter == null)) {
//            return response;
//        }
//        Optional<List<PersonCondition>> personConditions = personConditionRepository
//                .searchPersonCondition(subject, code, encounter);
//        response.setResourceType("Bundle");
//        response.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
//        response.setType("searchset");
//        response.setLink(List.of(Link.builder()
//                .url(CONDITION_URL_1).relation("self").build(), Link.builder()
//                .url(CONDITION_URL_2).relation("next").build()));
//        List<Entry> enry = null;
//        if (personConditions.isPresent()) {
//            List<PersonCondition> conditions = personConditions.get();
//            response.setId(subject.toString());
//            response.setTotal(conditions.size());
//            enry = conditions.stream().map(ittr -> {
//                Entry entryObj = new Entry();
//                ConditionMaster conditionMaster = ittr.getConditionMaster();
//                entryObj.setFullUrl(CONDITION_URL_1);
//
//                entryObj.setResource(Resource.builder()
//                        .resourceType("Condition")
//                        .clinicalStatus(ClinicalStatus.builder().build())
//                        .verificationStatus(VerificationStatus.builder().build())
//                        .id(ittr.getRowId().toString())
//                        .meta(EntryMeta.builder().versionId("1").source("#II7YGAcYPBuLlrB3")
//                                .lastUpdated(String.valueOf(LocalDateTime.now())).build())
//                        .clinicalStatus(ClinicalStatus.builder().coding(List.of(Coding.builder()
//                                .code("active")
//                                .system("http://terminology.hl7.org/CodeSystem/condition-clinical")
//                                .display("Active").build())).build())
//                        .verificationStatus(VerificationStatus.builder().coding(List.of(Coding.builder()
//                                .code("confirmed")
//                                .system("http://terminology.hl7.org/CodeSystem/condition-ver-status")
//                                .display("Confirmed").build())).build())
//                        .severity(Severity.builder().coding(List.of(Coding.builder()
//                                .code(ittr.getSeverityCode())
//                                .system("http://snomed.info/sct")
//                                .display(ittr.getSeverity()).build())).build())
//                        .category(List.of(Category.builder()
//                                .coding(List.of(Coding.builder()
//                                        .system(CONDITION_SYSTEM)
//                                        .display(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getCategory() : "")
//                                        .code(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getCatCode() : "").build())).build()))
//                        .encounter(Encounter.builder()
//                                .reference("Encounter/" + ittr.getHadmId()).build())
//                        .subject(Subject.builder()
//                                .reference("Patient/" + ittr.getPerson().getSubjectId()).build())
//                        .recordedDate(LocalDateTime.now())
//                        .code(Code.builder()
//                                .text(!ObjectUtils.isEmpty(ittr.getConditionMaster()) ? ittr.getConditionMaster().getLongTitle() : "")
//                                .coding(List.of(Coding.builder()
//                                        .code(!ObjectUtils.isEmpty(ittr.getConditionMaster()) ? ittr.getConditionMaster().getIcd9Code() : "")
//                                        .display(!ObjectUtils.isEmpty(ittr.getConditionMaster()) ? ittr.getConditionMaster().getLongTitle() : "")
//                                        .system("http://hl7.org/fhir/sid/icd-10-cm")
//                                        .build())).build()).build());
//                entryObj.setSearch(Search.builder().mode("match").build());
//                return entryObj;
//            }).toList();
//        }
//        response.setEntry(enry);
//        return response;
//    }

}
