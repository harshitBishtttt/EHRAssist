package EHRAssist.service;

import EHRAssist.dto.request.PatientSearchRequest;
import EHRAssist.dto.request.metaRequest.AddressInfoRequest;
import EHRAssist.dto.request.metaRequest.NameInfoRequest;
import EHRAssist.dto.request.metaRequest.TelecomInfoRequest;
import EHRAssist.dto.response.PersonsResponse;
import EHRAssist.dto.response.SearchResponse;

import EHRAssist.dto.response.patientSearchR4Response.*;
import EHRAssist.model.Person;
import EHRAssist.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PatientService {

    @Value("${EHRassist.database.header}")
    private String databaseName;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonRepository patientRepository;

    private void appendClauseToQuery(StringBuilder query, String entity, String attribute) {
        query.append("(").append(entity).append(".").append(attribute).append(" IS NULL OR ").append(entity).append(".")
                .append(attribute).append(" = :").append(attribute).append(") and ");
    }

    private String createCustomJPQL(PatientSearchRequest request) {
        NameInfoRequest name = request.getNameRequest();
        AddressInfoRequest address = request.getAddress();
        TelecomInfoRequest telecom = request.getTelecom();

        StringBuilder query = new StringBuilder("SELECT DISTINCT p FROM Person p ");
        query.append("JOIN FETCH p.personAddress pa ");
        query.append("JOIN FETCH p.personName pn ");
        query.append("JOIN FETCH p.patientTelecom pt ");
        query.append("WHERE ");

        if (request.getGender() != null && !request.getGender().isEmpty()) {
            appendClauseToQuery(query, "p", "gender");
        }
        if (request.getBirthdate() != null) {
            appendClauseToQuery(query, "p", "birthdate");
        }
        if (name.getFamily() != null && !name.getFamily().isEmpty()) {
            appendClauseToQuery(query, "pn", "family");
        }
        if (name.getGiven() != null && !name.getGiven().isEmpty()) {
            appendClauseToQuery(query, "pn", "given");
        }
        if (name.getName() != null && !name.getName().isEmpty()) {
            appendClauseToQuery(query, "pn", "name");
        }
        if (address.getAddressCity() != null && !address.getAddressCity().isEmpty()) {
            appendClauseToQuery(query, "pa", "city");
        }
        if (address.getAddressPostalCode() != null && !address.getAddressPostalCode().isEmpty()) {
            appendClauseToQuery(query, "pa", "postalCode");
        }
        if (address.getAddressState() != null && !address.getAddressState().isEmpty()) {
            appendClauseToQuery(query, "pa", "state");
        }
        if (telecom.getUseTel() != null && !telecom.getUseTel().isEmpty()) {
            appendClauseToQuery(query, "pt", "useTel");
        }
        if (telecom.getSystem() != null && !telecom.getSystem().isEmpty()) {
            appendClauseToQuery(query, "pt", "system");
        }
        if (telecom.getValue() != null && !telecom.getValue().isEmpty()) {
            appendClauseToQuery(query, "pt", "value");
        }

        int len = query.length();
        if (len >= 4 && query.substring(len - 4).equals("and ")) {
            query.delete(len - 4, len);
        }

        return query.toString();
    }


    private Query setValueToJPQLQuery(String jpql, PatientSearchRequest request) {
        Query query = entityManager.createQuery(jpql, Person.class);
        NameInfoRequest name = request.getNameRequest();
        AddressInfoRequest address = request.getAddress();
        TelecomInfoRequest telecom = request.getTelecom();

        if (request.getGender() != null && !request.getGender().isEmpty()) {
            query.setParameter("gender", request.getGender());
        }
        if (request.getBirthdate() != null) {
            query.setParameter("birthdate", request.getBirthdate());
        }
        if (name.getFamily() != null && !name.getFamily().isEmpty()) {
            query.setParameter("family", name.getFamily());
        }
        if (name.getGiven() != null && !name.getGiven().isEmpty()) {
            query.setParameter("given", name.getGiven());
        }
        if (name.getName() != null && !name.getName().isEmpty()) {
            query.setParameter("name", name.getName());
        }
        if (address.getAddressCity() != null && !address.getAddressCity().isEmpty()) {
            query.setParameter("city", address.getAddressCity());
        }
        if (address.getAddressPostalCode() != null && !address.getAddressPostalCode().isEmpty()) {
            query.setParameter("postalCode", address.getAddressPostalCode());
        }
        if (address.getAddressState() != null && !address.getAddressState().isEmpty()) {
            query.setParameter("state", address.getAddressState());
        }
        if (telecom.getUseTel() != null && !telecom.getUseTel().isEmpty()) {
            query.setParameter("useTel", telecom.getUseTel());
        }
        if (telecom.getSystem() != null && !telecom.getSystem().isEmpty()) {
            query.setParameter("system", telecom.getSystem());
        }
        if (telecom.getValue() != null && !telecom.getValue().isEmpty()) {
            query.setParameter("value", telecom.getValue());
        }

        return query;
    }

    public PersonsResponse searchPersonWithDynamicQuery(PatientSearchRequest request) {
        PersonsResponse response = new PersonsResponse();
        String jpql = createCustomJPQL(request);
        Query query = setValueToJPQLQuery(jpql, request);
        List<Person> resultList = query.getResultList();
        System.out.println(resultList);
        return response;
    }

    public PersonsResponse searchPerson(PatientSearchRequest request) {
        PersonsResponse person = new PersonsResponse();
        NameInfoRequest name = request.getNameRequest();
        AddressInfoRequest address = request.getAddress();
        TelecomInfoRequest telecom = request.getTelecom();
        List<Person> data = patientRepository.findAllPersons(
                request.getGender(), request.getBirthdate(),
                name.getFamily(),
                name.getGiven(),
                name.getName(),
                address.getAddressCity(),
                address.getAddressState(),
                address.getAddressPostalCode(),
                address.getAddress(),  //line is address
                telecom.getSystem(),
                telecom.getValue());
        int count = data.size();
        person.setResourceType("Bundel");
        person.setTotal(count);
        person.setType("Patient.Search (Demographics) (R4) ");
        List<EntityResponse> entityResponses = data.stream().map(ittr -> {
            List<AddressResponse> addressResponses = ittr.getPersonAddress().stream().map(add -> {
                return AddressResponse.builder()
                        .country(add.getCountry())
                        .line(add.getLine())
                        .postalCode(add.getPostalCode())
                        .state(add.getState())
                        .city(add.getCity())
                        .build();
            }).toList();
            List<NameResponse> nameResponse = ittr.getPersonName().stream().map(nameObj -> {
                return NameResponse.builder().use(nameObj.getName()).family(nameObj.getFamily()).build();
            }).toList();
            List<TelecomResponse> telecomResponses = ittr.getPatientTelecom().stream().map(tel -> {
                return TelecomResponse.builder()
                        .use(tel.getUseTel())
                        .system(tel.getSystem())
                        .value(tel.getValue())
                        .build();
            }).toList();

            EntityResponse entityResponse = new EntityResponse();
            entityResponse.setFullUrl("");
            entityResponse.setResource(ResourceResponse.
                    builder()
                    .resourceType("Person")
                    .id(ittr.getRowId())
                    .address(addressResponses)
                    .name(nameResponse)
                    .identifier(Arrays.asList(new IdentifierResponse())) // This field is incomplete
                    .telecom(telecomResponses)
                    .build());
            entityResponse.setSearchResponse(count > 0 ? SearchResponse.builder()
                    .mode("Matched").build() : SearchResponse.builder().mode("Non -Mtched").build());
            return entityResponse;
        }).toList();
        person.setEntry(entityResponses);
        return person;
    }


}
