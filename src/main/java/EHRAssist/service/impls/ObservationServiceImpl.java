//package EHRAssist.service.impls;
//
//import EHRAssist.dto.request.ObservationRequest;
//import EHRAssist.dto.response.PatientObservationResponse;
//import EHRAssist.dto.response.patientObservationResponse.*;
//import EHRAssist.repository.EHRAssistQueryDao.ObservationDao;
//import EHRAssist.repository.PersonRepository;
//import jakarta.persistence.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//
//@Service
//public class ObservationServiceImpl {
//
//    @Autowired
//    private ObservationDao observationDao;
//    @Autowired
//    private PersonRepository personRepository;
//
//    public PatientObservationResponse getPersonObservations(ObservationRequest request, Pageable page) {
//        PatientObservationResponse observationResponse = new PatientObservationResponse();
//        String nativeQuery = observationDao.getNativeObservationQuery(request);
//        Query query = observationDao.setValueToNativeObservationQuery(nativeQuery, request);
//        List<Object[]> resultList = query.getResultList();
//        Integer subjectId = Integer.parseInt(request.getSubject().getIdentifier());
//
//        List<Entry> entry = null;
//        if (!resultList.isEmpty()) {
//            entry = resultList.stream().map(ittr -> {
//                return Entry.builder()
//                        .resource(Resource.builder().id((Integer) resultList.get(0)[0])
//                                .subject(Subject.builder().reference("Patient/" + resultList.get(0)[1]).build())
//                                .status("final")
//                                .resourceType("Observation")
//                                .code(Code.builder()
//                                        .coding(Collections.singletonList(Coding.builder().text((String) resultList.get(0)[9])
//                                                .code((String) resultList.get(0)[13])
//                                                .system("http://loinc.org").build())).build())
//                                .encounter(!ObjectUtils.isEmpty(resultList.get(0)[2]) ? "Encounter/" + (String) resultList.get(0)[2] : "Encounter/56743")
//                                .effectiveDateTime((LocalDateTime) resultList.get(0)[4])
//                                .valueQuantity(ValueQuantity.builder()
//                                        .value((Double) resultList.get(0)[6])
//                                        .unit((String) resultList.get(0)[7]).code((String) resultList.get(0)[7]).system("http://unitsofmeasure.org")
//                                        .build())
//                                .specimen(Specimen.builder().display((String) resultList.get(0)[11])
//                                        .reference("Specimen/" + (String) resultList.get(0)[12]).build())
//                                .build()).build();
//            }).toList();
//        }
//        observationResponse.setResourceType("Bundle");
//        observationResponse.setTotal(resultList.size());
//        observationResponse.setEntry(entry);
//        observationResponse.setType("searchset");
//        return observationResponse;
//    }
//
//}
