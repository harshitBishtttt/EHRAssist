package EHRAssist.service;

import EHRAssist.dto.request.PersonRequest;
import EHRAssist.dto.request.personMetaRequest.AddressInfoRequest;
import EHRAssist.dto.request.personMetaRequest.NameInfoRequest;
import EHRAssist.dto.request.personMetaRequest.TelecomInfoRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PersonSearchService {

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

    private String createCustomJPQL(PersonRequest request) {
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
        if (name.getFirstName() != null && !name.getFirstName().isEmpty()) {
            appendClauseToQuery(query, "pn", "first_name");
        }
        if (name.getMiddleName() != null && !name.getMiddleName().isEmpty()) {
            appendClauseToQuery(query, "pn", "middle_name");
        }
        if (name.getLastName() != null && !name.getLastName().isEmpty()) {
            appendClauseToQuery(query, "pn", "last_name");
        }
        if (address.getAddressOne() != null && !address.getAddressOne().isEmpty()) {
            appendClauseToQuery(query, "pa", "address_one");
        }
        if (address.getAddressTwo() != null && !address.getAddressTwo().isEmpty()) {
            appendClauseToQuery(query, "pa", "address_two");
        }
        if (address.getAddressThree() != null && !address.getAddressThree().isEmpty()) {
            appendClauseToQuery(query, "pa", "address_three");
        }
        if (address.getPostalCode() != null && !address.getPostalCode().isEmpty()) {
            appendClauseToQuery(query, "pa", "postal_code");
        }
        if (address.getCountry() != null && !address.getCountry().isEmpty()) {
            appendClauseToQuery(query, "pa", "country");
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


    private Query setValueToJPQLQuery(String jpql, PersonRequest request) {
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
        if (name.getFirstName() != null && !name.getFirstName().isEmpty()) {
            query.setParameter("first_name", name.getFirstName());
        }
        if (name.getMiddleName() != null && !name.getMiddleName().isEmpty()) {
            query.setParameter("middle_name", name.getMiddleName());
        }
        if (name.getLastName() != null && !name.getLastName().isEmpty()) {
            query.setParameter("last_name", name.getLastName());
        }
        if (address.getAddressOne() != null && !address.getAddressOne().isEmpty()) {
            query.setParameter("address_one", address.getAddressOne());
        }
        if (address.getAddressTwo() != null && !address.getAddressTwo().isEmpty()) {
            query.setParameter("address_two", address.getAddressTwo());
        }
        if (address.getAddressThree() != null && !address.getAddressThree().isEmpty()) {
            query.setParameter("address_three", address.getAddressThree());
        }
        if (address.getPostalCode() != null && !address.getPostalCode().isEmpty()) {
            query.setParameter("postal_code", address.getPostalCode());
        }
        if (address.getCountry() != null && !address.getCountry().isEmpty()) {
            query.setParameter("country", address.getCountry());
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

    public PersonsResponse searchPersonWithDynamicQuery(PersonRequest request) {
        PersonsResponse response = new PersonsResponse();
        String jpql = createCustomJPQL(request);
        Query query = setValueToJPQLQuery(jpql, request);
        List<Person> resultList = query.getResultList();
        System.out.println(resultList);
        return response;
    }

    public PersonsResponse searchPerson(PersonRequest request, Pageable pageable) {
        PersonsResponse person = new PersonsResponse();
        NameInfoRequest name = request.getNameRequest();
        AddressInfoRequest address = request.getAddress();
        TelecomInfoRequest telecom = request.getTelecom();
        List<Person> data = patientRepository.findAllPersons(
                request.getGender(), request.getBirthdate(),
                name.getFirstName(),
                name.getMiddleName(),
                name.getLastName(),
                address.getAddressOne(),
                address.getAddressTwo(),
                address.getAddressThree(),
                address.getPostalCode(),  //line is address
                address.getCountry(),
                telecom.getSystem(),
                telecom.getValue(),
                telecom.getUseTel());
        int count = data.size();
        person.setResourceType("Bundel");
        person.setTotal(count);
        person.setType("Patient.Search (Demographics) (R4) ");
        List<EntityResponse> entityResponses = data.stream().map(ittr -> {
            List<AddressResponse> addressResponses = ittr.getPersonAddress().stream().map(add -> {
                return AddressResponse.builder()
                        .country(add.getCountry())
                        .line(add.getAddressOne())
                        .postalCode(add.getPostalCode())
                        .state(add.getAddressTwo())
                        .city(add.getAddressThree())
                        .build();
            }).toList();
            List<NameResponse> nameResponse = ittr.getPersonName().stream().map(nameObj -> {
                return NameResponse.builder().use(nameObj.getFirstName())
                        .given(Arrays.asList(nameObj.getFirstName(), nameObj.getMiddleName()))
                        .family(nameObj.getLastName()).build();
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
                    .id(ittr.getSubjectId())
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
