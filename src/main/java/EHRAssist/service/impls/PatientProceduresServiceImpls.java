package EHRAssist.service.impls;

import EHRAssist.repository.PersonProcedureRepository;
import EHRAssist.service.PatientProceduresService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PatientProceduresServiceImpls implements PatientProceduresService {

    @Autowired
    private PersonProcedureRepository personProcedureRepository;

    private Procedure mapToProcedure(Object[] obj) {
        String procedureCategory = (String) obj[9];
        Procedure procedure = new Procedure();
        procedure.setId(String.valueOf(obj[0]));
        procedure.setMeta(new Meta()
                .setVersionId("1")
                .setLastUpdated(Timestamp.valueOf("2025-12-16 07:13:12.674"))
                .setSource("#ep95K1tk1zpP15i7"));
        procedure.setStatus(Procedure.ProcedureStatus.COMPLETED);
        CodeableConcept category = new CodeableConcept();
        category.addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/procedure-category")
                .setCode(procedureCategory.toLowerCase().replace(" ", "-"))
                .setDisplay(procedureCategory));
        procedure.setCategory(category);
        CodeableConcept code = new CodeableConcept();
        code.setText((String) obj[10]);
        code.addCoding(new Coding()
                .setSystem("http://www.cms.gov/codes/cpt")
                .setCode(String.valueOf(obj[5]))
                .setDisplay((String) obj[9]));
        procedure.setCode(code);
        Reference reference = new Reference();
        reference.setReference("Patient/" + obj[1]);
        reference.setDisplay(!ObjectUtils.isEmpty(obj[24]) ? (obj[24]) + " " + (!ObjectUtils.isEmpty(obj[25]) ?
                (obj[25]) + " " + (!ObjectUtils.isEmpty(obj[26]) ? String.valueOf(obj[26]) : "") : "") : "");
        procedure.setSubject(reference);
        procedure.setEncounter(new Reference("Encounter/" + obj[2]));
        return procedure;
    }

    public Bundle getPatientProcedure(Integer subject,
                                      Integer encounter,
                                      Integer code,
                                      Pageable pageable) {

        List<Object[]> objects = personProcedureRepository.searchPersonProcedure(subject, encounter, code, pageable);
        Bundle bundle = new Bundle();
        bundle.setId(UUID.randomUUID().toString());
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(objects.size());
        bundle.setMeta(new Meta().setLastUpdated(new Date()));
        bundle.addLink()
                .setRelation("self")
                .setUrl("10.131.58.59:481/baseR4/Procedure?encounter=");

        for (Object[] row : objects) {
            Procedure procedure = mapToProcedure(row);
            Bundle.BundleEntryComponent entry = bundle.addEntry();
            entry.setFullUrl("https://hapi.fhir.org/baseR4/Procedure/53585775");
            entry.setResource(procedure);
            entry.setSearch(new Bundle.BundleEntrySearchComponent()
                    .setMode(Bundle.SearchEntryMode.MATCH));
        }
        return bundle;
    }

    //old snippet
//    private Resource resourceMapper(Object[] obj) {
//        String procedureCategory = (String) obj[9];
//        return Resource.builder()
//                .id(((Integer) obj[0]).toString())
//                .resourceType("Procedure")
//                .status("completed")
//                .meta(EntryMeta.builder().source("#ep95K1tk1zpP15i7").versionId("1").lastUpdated("2025-12-16T07:13:12.674+00:00").build())
//                .category(Category.builder().coding(List.of(Coding.builder()
//                        .code(!ObjectUtils.isEmpty(procedureCategory) ? procedureCategory.toLowerCase()
//                                .replace(" ", "-") : "")
//                        .display(procedureCategory)
//                        .system("http://terminology.hl7.org/CodeSystem/procedure-category").build())).build())
//                .code(Code.builder().coding(List.of(Coding.builder().system("http://www.cms.gov/codes/cpt")
//                        .code("" + obj[5]).display((String) obj[9]).build())).text((String) obj[10]).build())
//                .encounter(Encounter.builder().reference("Encounter/" + obj[2]).build())
//                .subject(Subject.builder().reference("Patient/" + obj[1]).build()).build();
//    }
//
//    public PatientProcedureResponse getPatientProcedure(Integer subject, Integer encounter, Integer code, Pageable pageable) {
//        PatientProcedureResponse response = new PatientProcedureResponse();
//        List<Object[]> objects = personProcedureRepository.searchPersonProcedure(subject, encounter, code);
//        response.setResourceType("Bundle");
//        response.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
//        response.setType("searchset");
//        response.setLink(Collections.singletonList(Link.builder().url("10.131.58.59:481/baseR4/Procedure?encounter=").relation("self").build()));
//        List<Entry> entry = objects.stream().map(ittr -> {
//            Entry obj = new Entry();
//            obj.setFullUrl("https://hapi.fhir.org/baseR4/Procedure/53585775");
//            obj.setResource(resourceMapper(ittr));
//            obj.setSearch(Search.builder().mode("match").build());
//            return obj;
//        }).toList();
//        response.setEntry(entry);
//        response.setId(subject.toString());
//        response.setTotal(entry.size());
//        return response;
//    }
}
