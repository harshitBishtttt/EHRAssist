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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        response.setLink(Collections.singletonList(Link.builder().url("").relation("self").build()));
        List<Entry> enry = null;
        if (personConditions.isPresent()) {
            List<PersonCondition> conditions = personConditions.get();
            response.setId(subject.toString());
            response.setTotal(conditions.size());
            enry = conditions.stream().map(ittr -> {
                Entry entryObj = new Entry();
                ConditionMaster conditionMaster = ittr.getConditionMaster();
                entryObj.setFullUrl("/baseR4/Condition/");
                entryObj.setResource(Resource.builder()
                        .resourceType("Condition")
                        .meta(EntryMeta.builder().lastUpdate(null).source(null).versionId(null).build())
                        .clinicalStatus(ClinicalStatus.builder().build())
                        .verificationStatus(VerificationStatus.builder().build())
                        .id(ittr.getRowId())
                        .category(Category.builder()
                                .coding(List.of(Coding.builder()
                                        .system("http://terminology.hl7.org/CodeSystem/condition-category")
                                        .display(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getShortTitle() : "")
                                        .code(!ObjectUtils.isEmpty(conditionMaster) ? conditionMaster.getIcd9Code() : "").build())).build())
                        .encounter(Encounter.builder()
                                .reference("Encounter/" + ittr.getHadmId()).build())
                        .subject(Subject.builder()
                                .reference("Patient/" + ittr.getPerson().getSubjectId()).build())
                        .recordedDate(null)
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
