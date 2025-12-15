package EHRAssist.service;

import EHRAssist.dto.request.ObservationRequest;
import EHRAssist.dto.response.observationResponse.Patient;
import EHRAssist.dto.response.observationResponse.PersonObservationResponse;
import EHRAssist.dto.response.observationResponse.Test;
import EHRAssist.model.Person;
import EHRAssist.repository.EHRAssistQueryDao.ObservationDao;
import EHRAssist.repository.PersonRepository;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        observationResponse.setResourceType("Observation");
        observationResponse.setTotal(resultList.size());
        Person getPersonName = personRepository.findBySubjectId((Integer) resultList.get(0)[0]);
        observationResponse.setPatient(getPersonName.getPersonName().stream().map(ittr -> {
            Patient obj = new Patient();
            obj.setFirstName(ittr.getFirstName());
            obj.setMiddleName(ittr.getMiddleName());
            obj.setLastName(ittr.getLastName());
            return obj;
        }).toList());
        if (!resultList.isEmpty()) {
            observationResponse.setTests(resultList.stream().map(ittr -> {
                Test obj = new Test();
                obj.setName((String) ittr[8]);
                obj.setValue((Double) ittr[5]);
                obj.setDate((LocalDateTime) ittr[3]);
                obj.setUom((String) ittr[6]);
                return obj;
            }).toList());
        }
        return observationResponse;
    }

}
