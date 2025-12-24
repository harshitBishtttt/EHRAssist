package EHRAssist.service.impls;


import EHRAssist.dto.response.PatientConditionResponse;
import EHRAssist.dto.response.patientConditionResponse.*;
import EHRAssist.model.ConditionMaster;
import EHRAssist.model.PersonCondition;
import EHRAssist.repository.PersonConditionRepository;
import EHRAssist.service.PatientConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static EHRAssist.utils.ApplicationConstants.*;

@Service
public class PatientConditionServiceImpl implements PatientConditionService {

    @Autowired
    private PersonConditionRepository personConditionRepository;


    public PatientConditionResponse getPatientCondition(Integer subject,
                                                        String code,
                                                        Integer encounter,
                                                        Pageable pageable) {
        PatientConditionResponse response = new PatientConditionResponse();
        if ((subject == null) && ((code == null) || code.isEmpty()) && (encounter == null)) {
            return response;
        }
        Optional<List<PersonCondition>> personConditions = personConditionRepository
                .searchPersonCondition(subject, code, encounter);
        response.setResourceType("Bundle");
        response.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
        response.setType("searchset");
        response.setLink(List.of(Link.builder()
                .url(CONDITION_URL_1).relation("self").build(), Link.builder()
                .url(CONDITION_URL_2).relation("next").build()));
        List<Entry> enry = null;
        if (personConditions.isPresent()) {
            List<PersonCondition> conditions = personConditions.get();
            response.setId(subject.toString());
            response.setTotal(conditions.size());
            enry = conditions.stream().map(ittr -> {
                Entry entryObj = new Entry();
                ConditionMaster conditionMaster = ittr.getConditionMaster();
                entryObj.setFullUrl(CONDITION_URL_1);
                entryObj.setResource(Resource.builder()
                        .resourceType("Condition")
                        .clinicalStatus(ClinicalStatus.builder().build())
                        .verificationStatus(VerificationStatus.builder().build())
                        .id(ittr.getRowId())
                        .meta(EntryMeta.builder().versionId("1").source("#II7YGAcYPBuLlrB3")
                                .lastUpdated(String.valueOf(LocalDateTime.now())).build())
                        .clinicalStatus(ClinicalStatus.builder().coding(List.of(Coding.builder()
                                .code("active")
                                .system("http://terminology.hl7.org/CodeSystem/condition-clinical")
                                .display("Active").build())).build())
                        .verificationStatus(VerificationStatus.builder().coding(List.of(Coding.builder()
                                .code("confirmed")
                                .system("http://terminology.hl7.org/CodeSystem/condition-ver-status")
                                .display("Confirmed").build())).build())
                        .severity(Severity.builder().coding(List.of(Coding.builder()
                                .code("24484000")
                                .system("http://snomed.info/sct")
                                .display("Severe").build())).build())
                        .category(List.of(Category.builder()
                                .coding(List.of(Coding.builder()
                                        .system(CONDITION_SYSTEM)
                                        .display(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getShortTitle() : "")
                                        .code(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getIcd9Code() : "").build())).build()))
                        .encounter(Encounter.builder()
                                .reference("Encounter/" + ittr.getHadmId()).build())
                        .subject(Subject.builder()
                                .reference("Patient/" + ittr.getPerson().getSubjectId()).build())
                        .recordedDate(LocalDateTime.now())
                        .code(Code.builder()
                                .text(!ObjectUtils.isEmpty(ittr.getConditionMaster()) ? ittr.getConditionMaster().getLongTitle() : "")
                                .coding(List.of(Coding.builder()
                                        .code(!ObjectUtils.isEmpty(ittr.getConditionMaster()) ? ittr.getConditionMaster().getIcd9Code() : "")
                                        .display(!ObjectUtils.isEmpty(ittr.getConditionMaster()) ? ittr.getConditionMaster().getLongTitle() : "")
                                        .system("http://hl7.org/fhir/sid/icd-10-cm")
                                        .build())).build()).build());
                entryObj.setSearch(Search.builder().mode("matched").build());
                return entryObj;
            }).toList();
        }
        response.setEntry(enry);
        return response;
    }
}
