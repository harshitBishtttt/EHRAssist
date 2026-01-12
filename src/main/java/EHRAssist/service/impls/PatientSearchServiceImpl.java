package EHRAssist.service.impls;


import EHRAssist.model.Person;
import EHRAssist.model.VisitAdmissions;
import EHRAssist.repository.PersonRepository;
import EHRAssist.service.PatientSearchService;
import EHRAssist.utils.ApplicationConstants;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientSearchServiceImpl implements PatientSearchService {
    @Autowired
    private PersonRepository personRepository;
    private final FhirContext fhirContext = FhirContext.forR4();

//    public Resource resourceMapper(Person person) {
//        Resource resource = new Resource();
//        VisitAdmissions visitAdmissions = person.getVisitAdmissions();
//        resource.setResourceType("Patient");
//        resource.setId(String.valueOf(person.getSubjectId()));
//        //resource.setActive(person.getDod().isAfter(LocalDateTime.now()));
//        resource.setMeta(EntryMeta.builder()
//                .lastUpdated(String.valueOf(person.getLoadedAt()))
//                .source("#SPMeDNKMZT33s52w")
//                .versionId("1").build());
//        resource.setText(Text.builder().div(ApplicationConstants.DIV).status("generated").build());
//        if (!ObjectUtils.isEmpty(person.getExtensions())) {
//            resource.setExtension(person.getExtensions().stream().map(ittr -> {
//                return Extension.builder().valueString(ittr.getValueString()).url(ittr.getUrl()).build();
//            }).toList());
//        }
//        resource.setExtension(List.of(Extension.builder()
//                        .url("attachedDocument")
//                        .valueString(person.getSourceFile()).build(),
//                Extension.builder()
//                        .url("educationQualification")
//                        .valueString("Bachelor’s degree (e.g. BA, BS)").build(),
//                Extension.builder()
//                        .url("employmentStatus")
//                        .valueString(!ObjectUtils.isEmpty(visitAdmissions) ? visitAdmissions.getMaritalStatus() : "").build(),
//                Extension.builder()
//                        .url("ethnicity")
//                        .valueString(!ObjectUtils.isEmpty(visitAdmissions) ? visitAdmissions.getEthnicity() : "").build(),
//                Extension.builder()
//                        .url("race")
//                        .valueString(!ObjectUtils.isEmpty(visitAdmissions) ? visitAdmissions.getReligion() : "").build()));
//        if (!ObjectUtils.isEmpty(person.getPersonName())) {
//            resource.setName(person.getPersonName().stream().map(ittr -> {
//                return NameResponse.builder()
//                        .use("official")
//                        .given(Arrays.asList(ittr.getFirstName(), ittr.getMiddleName()))
//                        .family(ittr.getLastName()).build();
//            }).toList());
//        }
//        if (!ObjectUtils.isEmpty(person.getPersonTelecom())) {
//            List<Object> obj = new ArrayList<>();
//            person.getPersonTelecom().forEach(ittr -> {
//                if (ittr.getSystem().equalsIgnoreCase("phone")) {
//                    obj.add(PhoneTelecom.builder()
//                            .value(ittr.getValue())
//                            .system(ittr.getSystem()).use("mobile").build());
//                } else {
//                    obj.add(TelecomResponse.builder()
//                            .system(ittr.getSystem())
//                            .value(ittr.getValue())
//                            .build());
//                }
//            });
//            resource.setTelecom(obj);
//        }
//        resource.setGender(!ObjectUtils.isEmpty(person.getGender()) ? person.getGender().toLowerCase() : "");
//        resource.setBirthDate(person.getBirthdate());
//        if (!ObjectUtils.isEmpty(person.getPersonAddress())) {
//            resource.setAddress(person.getPersonAddress().stream().map(ittr -> {
//                return Address.builder()
//                        .city(ittr.getCity())
//                        .state(ittr.getState())
//                        .line(Arrays.asList(ittr.getAddressOne(), ittr.getAddressTwo(), ittr.getCity()))
//                        .postalCode(ittr.getPostalCode()).build();
//            }).toList());
//        }
//
//        String marStatus = !ObjectUtils.isEmpty(visitAdmissions) &&
//                !ObjectUtils.isEmpty(visitAdmissions.getMaritalStatus())
//                ? visitAdmissions.getMaritalStatus().substring(0, 1).toUpperCase() +
//                visitAdmissions.getMaritalStatus().substring(1).toLowerCase() : null;
//        if (!ObjectUtils.isEmpty(visitAdmissions)) {
//            resource.setMaritalStatus(MaritalStatus.builder()
//                    .text(marStatus)
//                    .coding(Collections.singletonList(Coding.builder()
//                            .code(!ObjectUtils.isEmpty(marStatus) ? "" + marStatus.charAt(0) : "")
//                            .display(marStatus)
//                            .system("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus")
//                            .build())).build());
//            Communication communication = new Communication();
//            Language language = Language.builder().text(person.getLanguage())
//                    .coding(Collections.singletonList(Coding.builder()
//                            .system("urn:ietf:bcp:47")
//                            .code(person.getLangCode())
//                            .display(person.getLanguage())
//                            .build())).build();
//            communication.setLanguage(language);
//            communication.setPreferred(true);
//            resource.setCommunication(List.of(communication));
//        }
//
//        return resource;
//    }
//
//    public PatientSearchResponse searchPatient(String family,
//                                               String given,
//                                               String email,
//                                               String phone,
//                                               LocalDate birthdate,
//                                               String gender,
//                                               Pageable pageable) {
//        PatientSearchResponse searchResponse = new PatientSearchResponse();
//        searchResponse.setId(UUID.randomUUID().toString());
//        Optional<List<Person>> getPersons = personRepository
//                .searchPerson(family, given, email, phone, birthdate, gender);
//        searchResponse.setResourceType("Bundle");
//        searchResponse.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
//        searchResponse.setType("searchset");
//        searchResponse.setLink(Collections.singletonList(Link.builder()
//                .url("10.131.58.59:481/baseR4/Patient?family=" + family + "&given=" + given + "&email=" + email + "&birthdate=" + birthdate + "&gender=" + gender)
//                .relation("self").build()));
//        if (getPersons.isPresent()) {
//            List<Person> people = getPersons.get();
//            List<Entry> matched = people.stream().map(ittr -> {
//                return Entry.builder().fullUrl("https://hapi.fhir.org/baseR4/Patient/" + ittr.getSubjectId())
//                        .resource(resourceMapper(ittr)).search(SearchResponse.builder().mode("matched").build()).build();
//            }).toList();
//
//            searchResponse.setTotal(matched.size());
//            searchResponse.setEntry(matched);
//        }
//        return searchResponse;
//    }
//
//    public PatientSearchResponse searchPatientByEmail(String email, Pageable pageable) {
//        PatientSearchResponse searchResponse = new PatientSearchResponse();
//        Optional<Person> byPersonAddressEmail = personRepository
//                .findByPersonTelecom_SystemAndPersonTelecom_Value("email", email);
//        searchResponse.setResourceType("Bundle");
//        searchResponse.setMeta(Meta.builder().lastUpdated(String.valueOf(LocalDateTime.now())).build());
//        searchResponse.setType("searchset");
//        searchResponse.setLink(Collections.singletonList(Link.builder().url("").relation("self").build()));
//        Entry entryObject = new Entry();
//        if (byPersonAddressEmail.isPresent()) {
//            Person person = byPersonAddressEmail.get();
//            entryObject.setFullUrl("https://hapi.fhir.org/baseR4/Patient/" + person.getSubjectId());
//            entryObject.setResource(resourceMapper(person));
//            entryObject.setSearch(SearchResponse.builder().mode("matched").build());
//            searchResponse.setId(String.valueOf(person.getRowId()));
//            searchResponse.setTotal(1);
//            searchResponse.setEntry(List.of(entryObject));
//        } else {
//            searchResponse.setTotal(0);
//            entryObject.setSearch(SearchResponse.builder().mode("non-matched").build());
//        }
//        return searchResponse;
//    }

    //........................................................................................................


    private Patient mapToFhirPatient(Person person) {

        Patient patient = new Patient();
        patient.setId(String.valueOf(person.getSubjectId()));
        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdatedElement(new InstantType(person.getLoadedAt().toString()));
        meta.setSource("#SPMeDNKMZT33s52w");
        patient.setMeta(meta);
        Narrative narrative = new Narrative();
        narrative.setStatus(Narrative.NarrativeStatus.GENERATED);
        narrative.setDivAsString(ApplicationConstants.DIV);
        patient.setText(narrative);
        if (!ObjectUtils.isEmpty(person.getPersonName())) {
            person.getPersonName().forEach(n -> {
                HumanName name = new HumanName();
                name.setUse(HumanName.NameUse.OFFICIAL);
                name.setFamily(n.getLastName());
                name.addGiven(n.getFirstName());
                name.addGiven(n.getMiddleName());
                patient.addName(name);
            });
        }
        if (!ObjectUtils.isEmpty(person.getPersonTelecom())) {
            person.getPersonTelecom().forEach(t -> {
                ContactPoint telecom = new ContactPoint();
                telecom.setSystem(ContactPoint.ContactPointSystem.fromCode(t.getSystem()));
                telecom.setValue(t.getValue());

                if ("phone".equalsIgnoreCase(t.getSystem())) {
                    telecom.setUse(ContactPoint.ContactPointUse.MOBILE);
                }

                patient.addTelecom(telecom);
            });
        }
        if (!ObjectUtils.isEmpty(person.getGender())) {
            patient.setGender(Enumerations.AdministrativeGender.fromCode(person.getGender().toLowerCase()));
        }
        if (!ObjectUtils.isEmpty(person.getBirthdate())) {
            patient.setBirthDate(java.sql.Date.valueOf(person.getBirthdate()));
        }
        if (!ObjectUtils.isEmpty(person.getPersonAddress())) {
            person.getPersonAddress().forEach(a -> {
                Address address = new Address();
                address.addLine(a.getAddressOne());
                address.addLine(a.getAddressTwo());
                address.addLine(a.getCity());
                address.setCity(a.getCity());
                address.setState(a.getState());
                address.setPostalCode(a.getPostalCode());
                patient.addAddress(address);
            });
        }
        VisitAdmissions visit = person.getVisitAdmissions();
        if (!ObjectUtils.isEmpty(visit) && !ObjectUtils.isEmpty(visit.getMaritalStatus())) {

            Coding coding = new Coding();
            coding.setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus");
            coding.setCode(visit.getMaritalStatus().substring(0, 1).toUpperCase());
            coding.setDisplay(capitalize(visit.getMaritalStatus()));

            CodeableConcept maritalStatus = new CodeableConcept();
            maritalStatus.addCoding(coding);
            maritalStatus.setText(capitalize(visit.getMaritalStatus()));

            patient.setMaritalStatus(maritalStatus);
        }
        if (!ObjectUtils.isEmpty(person.getLanguage())) {

            Coding langCoding = new Coding();
            langCoding.setSystem("urn:ietf:bcp:47");
            langCoding.setCode(person.getLangCode());
            langCoding.setDisplay(person.getLanguage());

            CodeableConcept language = new CodeableConcept();
            language.addCoding(langCoding);
            language.setText(person.getLanguage());

            Patient.PatientCommunicationComponent communication = new Patient.PatientCommunicationComponent();
            communication.setLanguage(language);
            communication.setPreferred(true);

            patient.addCommunication(communication);
        }
        patient.addExtension(new Extension("attachedDocument", new StringType(person.getSourceFile())));
        patient.addExtension(new Extension("educationQualification", new StringType("Bachelor’s degree (e.g. BA, BS)")));

        if (!ObjectUtils.isEmpty(visit)) {
            patient.addExtension(new Extension("employmentStatus", new StringType(visit.getMaritalStatus())));
            patient.addExtension(new Extension("ethnicity", new StringType(visit.getEthnicity())));
            patient.addExtension(new Extension("race", new StringType(visit.getReligion())));
        }

        return patient;
    }


    public Bundle searchPatient(String family,
                                String given,
                                String email,
                                String phone,
                                LocalDate birthdate,
                                String gender,
                                Pageable pageable) {

        Optional<List<Person>> persons = personRepository
                .searchPerson(family, given, email, phone, birthdate, gender);

        Bundle bundle = new Bundle();
        bundle.setId(UUID.randomUUID().toString());
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(persons.map(List::size).orElse(0));

        // Meta
        Meta meta = new Meta();
        meta.setLastUpdatedElement(new InstantType(LocalDateTime.now().toString()));
        bundle.setMeta(meta);

        // Self link
        bundle.addLink()
                .setRelation("self")
                .setUrl("10.131.58.59:481/baseR4/Patient?family=" + family +
                        "&given=" + given +
                        "&email=" + email +
                        "&birthdate=" + birthdate +
                        "&gender=" + gender);

        // Entries
        persons.ifPresent(list -> {
            list.forEach(p -> {
                Patient patient = mapToFhirPatient(p);

                Bundle.BundleEntryComponent entry = bundle.addEntry();
                entry.setFullUrl("https://hapi.fhir.org/baseR4/Patient/" + p.getSubjectId());
                entry.setResource(patient);

                Bundle.BundleEntrySearchComponent search = new Bundle.BundleEntrySearchComponent();
                search.setMode(Bundle.SearchEntryMode.MATCH);
                entry.setSearch(search);
            });
        });

        return bundle;
    }


    private String capitalize(String str) {
        if (ObjectUtils.isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


}
