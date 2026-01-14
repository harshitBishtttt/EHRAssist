package EHRAssist.service.impls;

import EHRAssist.model.ConditionMaster;
import EHRAssist.model.PersonCondition;
import EHRAssist.repository.PersonConditionRepository;
import EHRAssist.service.PatientConditionService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientConditionServiceImpl implements PatientConditionService {

    @Autowired
    private PersonConditionRepository personConditionRepository;

    public Bundle getPatientCondition(Integer subject,
                                      String code,
                                      Integer encounter,
                                      Pageable pageable) {

        Bundle bundle = new Bundle();

        if (subject == null && (code == null || code.isEmpty()) && encounter == null) {
            return bundle;
        }

        Optional<List<PersonCondition>> personConditions =
                personConditionRepository.searchPersonCondition(subject, code, encounter);

        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setId(subject.toString());
        bundle.setTotal(personConditions.map(List::size).orElse(0));
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

        for (PersonCondition pc : personConditions.get()) {
            Condition condition = new Condition();
            condition.setId(pc.getRowId().toString());
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
                            .setCode(pc.getSeverityCode())
                            .setDisplay(pc.getSeverity())
            ));
            ConditionMaster conditionMaster = pc.getConditionMaster();
            condition.setCode(new CodeableConcept()
                    .setText(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getLongTitle() : "")
                    .addCoding(new Coding()
                            .setSystem("http://hl7.org/fhir/sid/icd-10-cm")
                            .setCode(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getIcd9Code() : "")
                            .setDisplay(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getLongTitle() : "")
                    ));
            condition.setSubject(new Reference("Patient/" + pc.getPerson().getSubjectId()));
            condition.setEncounter(new Reference("Encounter/" + pc.getHadmId()));
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
