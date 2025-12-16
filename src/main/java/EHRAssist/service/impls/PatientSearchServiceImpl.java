package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientSearchResponse;
import EHRAssist.dto.response.patientSearchResponse.*;
import EHRAssist.model.Person;
import EHRAssist.repository.PersonRepository;
import EHRAssist.service.PatientSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PatientSearchServiceImpl implements PatientSearchService {
    @Autowired
    private PersonRepository personRepository;

    private Resource resourceMapper(Person person) {
        Resource resource = new Resource();
        resource.setResourceType("Patient");
        resource.setId(person.getSubjectId());
        resource.setMeta(EntryMeta.builder()
                .lastUpdated(String.valueOf(person.getLoadedAt()))
                .source("#SPMeDNKMZT33s52w")
                .versionId("1").build());
        resource.setText(Text.builder().build());
        if (!ObjectUtils.isEmpty(person.getExtensions())) {
            resource.setExtension(person.getExtensions().stream().map(ittr -> {
                return Extension.builder().valueString(ittr.getValueString()).url(ittr.getUrl()).build();
            }).toList());
        }
        if (!ObjectUtils.isEmpty(person.getPersonName())) {
            resource.setName(person.getPersonName().stream().map(ittr -> {
                return NameResponse.builder()
                        .use(ittr.getFirstName())
                        .given(Arrays.asList(ittr.getMiddleName(), ittr.getLastName()))
                        .family(ittr.getLastName()).build();
            }).toList());
        }
        if (!ObjectUtils.isEmpty(person.getPersonTelecom())) {
            resource.setTelecom(person.getPersonTelecom().stream().map(ittr -> {
                return TelecomResponse.builder()
                        .system(ittr.getSystem())
                        .use(ittr.getUseTel())
                        .value(ittr.getValue())
                        .build();
            }).toList());
        }
        resource.setGender(person.getGender());
        resource.setBirthDate(person.getBirthdate());
        if (!ObjectUtils.isEmpty(person.getPersonAddress())) {
            resource.setAddress(person.getPersonAddress().stream().map(ittr -> {
                return Address.builder()
                        .city(ittr.getCity())
                        .state(ittr.getState())
                        .line(Arrays.asList(ittr.getAddressOne(), ittr.getAddressTwo(), ittr.getCity()))
                        .postalCode(ittr.getPostalCode()).build();
            }).toList());
        }

        resource.setMaritalStatus(MaritalStatus.builder()
                .text(person.getMaritalStatus()
                        .toUpperCase(Locale.ROOT))
                .coding(Collections.singletonList(Coding.builder().code(person.getMaritalCode())
                        .display(person.getMaritalStatus())
                        .system("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus")
                        .build())).build());

        if (!ObjectUtils.isEmpty(person.getPersonLanguages())) {
            resource.setCommunication(person.getPersonLanguages().stream().map(ittr -> {
                Communication communication = new Communication();
                Language language = Language.builder().text(ittr.getLanguageName().toUpperCase(Locale.ROOT))
                        .coding(Collections.singletonList(Coding.builder()
                                .system("urn:ietf:bcp:47")
                                .code(ittr.getLanguageCode())
                                .display(ittr.getLanguageName())
                                .build())).build();
                communication.setLanguage(language);
                communication.setPreferred(ittr.getActive());
                return communication;
            }).toList());
        }
        return resource;
    }

    public PatientSearchResponse searchPatientByEmail(String email, Pageable pageable) {
        PatientSearchResponse searchResponse = new PatientSearchResponse();
        Optional<Person> byPersonAddressEmail = personRepository
                .findByPersonTelecom_SystemAndPersonTelecom_Value("email", email);
        searchResponse.setResourceType("Bundle");
        searchResponse.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
        searchResponse.setType("searchset");
        searchResponse.setLink(Collections.singletonList(Link.builder().url("").relation("self").build()));
        Entry entryObject = new Entry();
        if (byPersonAddressEmail.isPresent()) {
            Person person = byPersonAddressEmail.get();
            entryObject.setFullUrl("https://hapi.fhir.org/baseR4/Patient/" + person.getSubjectId());
            entryObject.setResource(resourceMapper(person));
            entryObject.setMode(SearchResponse.builder().mode("matched").build());
            searchResponse.setId(String.valueOf(person.getRowId()));
            searchResponse.setTotal(1);
            searchResponse.setEntry(List.of(entryObject));
        } else {
            searchResponse.setTotal(0);
            entryObject.setMode(SearchResponse.builder().mode("non-matched").build());
        }
        return searchResponse;
    }

}
