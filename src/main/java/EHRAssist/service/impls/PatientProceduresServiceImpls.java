package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientProcedureResponse;
import EHRAssist.dto.response.patientConditionResponse.EntryMeta;
import EHRAssist.dto.response.patientConditionResponse.Link;
import EHRAssist.dto.response.patientConditionResponse.Meta;
import EHRAssist.dto.response.personProcedureResponse.*;
import EHRAssist.repository.PersonProcedureRepository;
import EHRAssist.service.PatientProceduresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class PatientProceduresServiceImpls implements PatientProceduresService {

    @Autowired
    private PersonProcedureRepository personProcedureRepository;

    private Resource resourceMapper(Object[] obj) {
        return Resource.builder()
                .id((Integer) obj[0])
                .resourceType("Procedure")
                .status("complete")
                .meta(EntryMeta.builder().build())
                .category(Category.builder().coding(List.of(Coding.builder().code((String) obj[9])
                        .display((String) obj[10])
                        .system("http://terminology.hl7.org/CodeSystem/procedure-category").build())).build())
                .code(Code.builder().coding(List.of(Coding.builder().system("http://terminology.hl7.org/CodeSystem/procedure-category")
                        .code("" + obj[5]).display((String) obj[9]).build())).text((String) obj[10]).build())
                .encounter(Encounter.builder().reference("Encounter/" + obj[2]).build())
                .subject(Subject.builder().reference("Subject/" + obj[1]).build()).build();
    }

    public PatientProcedureResponse getPatientProcedure(Integer subject, Integer encounter, Integer code, Pageable pageable) {
        PatientProcedureResponse response = new PatientProcedureResponse();
        List<Object[]> objects = personProcedureRepository.searchPersonProcedure(subject, encounter, code);
        response.setResourceType("Bundle");
        response.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
        response.setType("searchset");
        response.setLink(Collections.singletonList(Link.builder().url("/baseR4/Procedure").relation("self").build()));
        List<Entry> entry = objects.stream().map(ittr -> {
            Entry obj = new Entry();
            obj.setFullUrl("https://hapi.fhir.org/baseR4/Procedure/53585775");
            obj.setResource(resourceMapper(ittr));
            obj.setSearch(Search.builder().mode("matched").build());
            return obj;
        }).toList();
        response.setEntry(entry);
        return response;
    }

}
