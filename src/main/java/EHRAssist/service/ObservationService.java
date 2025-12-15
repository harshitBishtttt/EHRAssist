package EHRAssist.service;

import EHRAssist.dto.request.ObservationRequest;

import EHRAssist.dto.response.PersonObservationResponse;
import EHRAssist.dto.response.observationResponse.*;
import EHRAssist.dto.response.searchR4Response.NameResponse;
import EHRAssist.model.Person;
import EHRAssist.repository.EHRAssistQueryDao.ObservationDao;
import EHRAssist.repository.PersonRepository;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ObservationService {

    @Autowired
    private ObservationDao observationDao;
    @Autowired
    private PersonRepository personRepository;

    public PersonObservationResponse getPersonObservations(ObservationRequest request, Pageable page) {
        PersonObservationResponse observationResponse = new PersonObservationResponse();
        String nativeQuery = observationDao.getNativeObservationQuery(request);
        Query query = observationDao.setValueToNativeObservationQuery(nativeQuery, request);
        List<Object[]> resultList = query.getResultList();
        Integer subjectId = Integer.parseInt(request.getSubject().getIdentifier());
        Person getPersonName = personRepository.findBySubjectId(subjectId);
        Subject name = new Subject();
        List<DataMap> dataMap = null;
        name.setId(subjectId);
        name.setNames(getPersonName.getPersonName().stream().map(ittr -> {
            NameResponse nameResponse = new NameResponse();
            nameResponse.setUse(ittr.getFirstName());
            nameResponse.setFamily(ittr.getLastName());
            nameResponse.setGiven(Arrays.asList(ittr.getMiddleName(), ittr.getLastName()));
            return nameResponse;
        }).toList());
        if (!resultList.isEmpty()) {
            dataMap = resultList.stream().map(ittr -> {
                return DataMap.builder()
                        .coding(Coding.builder().text((String) resultList.get(0)[8]).code((String) resultList.get(0)[11]).system("http://loinc.org").build())
                        .encounter(!ObjectUtils.isEmpty(resultList.get(0)[1]) ? (String) resultList.get(0)[1] : "")
                        .collectedDateTime((LocalDateTime) resultList.get(0)[3])
                        .valueQuantity(ValueQuantity.builder().value((Double) resultList.get(0)[5])
                                .unit((String) resultList.get(0)[6]).code("").system("http://unitsofmeasure.org").build())
                        .interpretation(Interpretation.builder()
                                .display(!ObjectUtils.isEmpty(resultList.get(0)[7]) ? (String) resultList.get(0)[7] : "")
                                .system("http://unitsofmeasure.org").build()).specimen((String) resultList.get(0)[10]).build();
            }).toList();
        }
        observationResponse.setResourceType("Observation");
        observationResponse.setTotal(resultList.size());
        observationResponse.setReference(name);
        observationResponse.setDataMap(dataMap);
        return observationResponse;
    }

}
