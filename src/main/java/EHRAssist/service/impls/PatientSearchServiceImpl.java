package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientSearchResponse;
import EHRAssist.dto.response.patientSearchResponse.*;
import EHRAssist.model.Person;
import EHRAssist.model.VisitAdmissions;
import EHRAssist.repository.PersonRepository;
import EHRAssist.service.PatientSearchService;
import EHRAssist.utils.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PatientSearchServiceImpl implements PatientSearchService {
    @Autowired
    private PersonRepository personRepository;

    public Resource resourceMapper(Person person) {
        Resource resource = new Resource();
        resource.setResourceType("Patient");
        resource.setId(person.getSubjectId());
        resource.setActive(person.getDod().isAfter(LocalDateTime.now()));
        resource.setMeta(EntryMeta.builder()
                .lastUpdated(String.valueOf(person.getLoadedAt()))
                .source("#SPMeDNKMZT33s52w")
                .versionId("1").build());
        resource.setText(Text.builder().div(ApplicationConstants.DIV).status("generated").build());
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
        VisitAdmissions visitAdmissions = person.getVisitAdmissions();
        if (!ObjectUtils.isEmpty(visitAdmissions)) {
            resource.setMaritalStatus(MaritalStatus.builder()
                    .text(visitAdmissions.getMaritalStatus())
                    .coding(Collections.singletonList(Coding.builder()
                            .code(String.valueOf(!ObjectUtils.isEmpty(visitAdmissions) ||
                                    !ObjectUtils.isEmpty(visitAdmissions.getMaritalStatus()) ?
                                    visitAdmissions.getMaritalStatus() : ""))
                            .display(!ObjectUtils.isEmpty(visitAdmissions) ||
                                    !ObjectUtils.isEmpty(visitAdmissions.getMaritalStatus())
                                    ? visitAdmissions.getMaritalStatus() : "")
                            .system("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus")
                            .build())).build());
            Communication communication = new Communication();
            Language language = Language.builder().text(visitAdmissions.getLanguage())
                    .coding(Collections.singletonList(Coding.builder()
                            .system("urn:ietf:bcp:47")
                            .code(visitAdmissions.getLanguage())
                            .display(visitAdmissions.getLanguage())
                            .build())).build();
            communication.setLanguage(language);
            communication.setPreferred(true);
            resource.setCommunication(List.of(communication));
        }

        return resource;
    }

    public PatientSearchResponse searchPatient(String family,
                                               String given,
                                               String email,
                                               String phone,
                                               LocalDate birthdate,
                                               String gender,
                                               Pageable pageable) {
        PatientSearchResponse searchResponse = new PatientSearchResponse();
        searchResponse.setId(UUID.randomUUID().toString());
        Optional<List<Person>> getPersons = personRepository
                .searchPerson(family, given, email, phone, birthdate, gender);
        searchResponse.setResourceType("Bundle");
        searchResponse.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
        searchResponse.setType("searchset");
        searchResponse.setLink(Collections.singletonList(Link.builder().url("").relation("self").build()));
        if (getPersons.isPresent()) {
            List<Person> people = getPersons.get();
            List<Entry> matched = people.stream().map(ittr -> {
                return Entry.builder().fullUrl("https://hapi.fhir.org/baseR4/Patient/" + ittr.getSubjectId())
                        .resource(resourceMapper(ittr)).search(SearchResponse.builder().mode("matched").build()).build();
            }).toList();

            searchResponse.setTotal(matched.size());
            searchResponse.setEntry(matched);
        }
        return searchResponse;
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
            entryObject.setSearch(SearchResponse.builder().mode("matched").build());
            searchResponse.setId(String.valueOf(person.getRowId()));
            searchResponse.setTotal(1);
            searchResponse.setEntry(List.of(entryObject));
        } else {
            searchResponse.setTotal(0);
            entryObject.setSearch(SearchResponse.builder().mode("non-matched").build());
        }
        return searchResponse;
    }

}
