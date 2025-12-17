package EHRAssist.service.impls;

import EHRAssist.dto.response.PatientSearchResponse;
import EHRAssist.dto.response.patientSearchResponse.*;
import EHRAssist.eHRAUtils.mappers.ResourceMapper;
import EHRAssist.model.Person;
import EHRAssist.repository.PersonRepository;
import EHRAssist.service.PatientSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PatientSearchServiceImpl implements PatientSearchService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ResourceMapper resourceMapper;

    public PatientSearchResponse searchPatient(String family,
                                               String given,
                                               String email,
                                               LocalDate birthdate,
                                               String gender,
                                               Pageable pageable) {
        PatientSearchResponse searchResponse = new PatientSearchResponse();
        Optional<List<Person>> getPersons = personRepository
                .searchPerson(family, given, email, birthdate, gender);
        searchResponse.setResourceType("Bundle");
        searchResponse.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
        searchResponse.setType("searchset");
        searchResponse.setLink(Collections.singletonList(Link.builder().url("").relation("self").build()));
        if (getPersons.isPresent()) {
            List<Person> people = getPersons.get();
            List<Entry> matched = people.stream().map(ittr -> {
                return Entry.builder().fullUrl("https://hapi.fhir.org/baseR4/Patient/" + ittr.getSubjectId())
                        .resource(resourceMapper.resourceMapper(ittr)).mode(SearchResponse.builder().mode("matched").build()).build();
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
            entryObject.setResource(resourceMapper.resourceMapper(person));
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
