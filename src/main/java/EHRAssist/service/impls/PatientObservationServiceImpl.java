package EHRAssist.service.impls;

import EHRAssist.dto.FhirQuantity;
import EHRAssist.dto.ObservationDto;
import EHRAssist.exceptionHandler.exceptions.MissingParametersException;
import EHRAssist.repository.EHRAssistQueryDao.ObservationDao;
import EHRAssist.service.PatientObservationService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PatientObservationServiceImpl implements PatientObservationService {

    @Autowired
    private ObservationDao observationDao;


    static String toVitalSignsCode(String input) {
        return input == null
                ? null
                : input.trim()
                .toLowerCase()
                .replaceAll("\\s+", "-");
    }

    private Observation buildObservation(Object[] ittr) {

        Observation obs = new Observation();
        obs.setId(ittr[0].toString());
        obs.getMeta()
                .setVersionId("1")
                .setLastUpdated(new Date())
                .setSource("#wnzvyWyVXFVJbcHX")
                .addProfile("http://hl7.org/fhir/StructureDefinition/vitalsigns");
        obs.getText()
                .setStatus(Narrative.NarrativeStatus.GENERATED)
                .setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">"
                        + ittr[9] + " observation (no performer)</div>");
        obs.addIdentifier()
                .setSystem("http://hospital.example.org/observations")
                .setValue("BP-2025-0002");
        obs.setStatus(Observation.ObservationStatus.FINAL);
        obs.addCategory()
                .addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/observation-category")
                .setCode(toVitalSignsCode((String) ittr[10]))
                .setDisplay((String) ittr[10]);

        obs.getCategoryFirstRep().setText((String) ittr[10]);
        obs.getCode()
                .addCoding()
                .setSystem("http://loinc.org")
                .setCode((String) ittr[12])
                .setDisplay((String) ittr[9]);

        obs.getCode().setText((String) ittr[9]);
        obs.setSubject(new Reference("Patient/" + ittr[1])
                .setDisplay(ittr[13] + " " + ittr[15]));
        obs.setEncounter(new Reference("Encounter/567865"));
        obs.setEffective(new DateTimeType(new Date()));
        obs.setIssued(new Date());
        obs.addInterpretation()
                .addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation")
                .setCode("N")
                .setDisplay("Normal");
        obs.addNote().setText("Patient seated and rested for 5 minutes before measurement.");
        CodeableConcept bodySiteConcept = new CodeableConcept();
        bodySiteConcept.addCoding(new Coding()
                .setSystem("http://snomed.info/sct")
                .setCode("368209003")
                .setDisplay("Right arm"));
        obs.setBodySite(bodySiteConcept);
        CodeableConcept methodConcept = new CodeableConcept();
        methodConcept.addCoding(new Coding()
                .setSystem("http://snomed.info/sct")
                .setCode("6525385")
                .setDisplay("anual sphygmomanometer"));
        obs.setMethod(methodConcept);
        Observation.ObservationComponentComponent component = obs.addComponent();
        component.getCode()
                .addCoding()
                .setSystem("http://loinc.org")
                .setCode((String) ittr[12])
                .setDisplay((String) ittr[9]);
        component.getCode().setText((String) ittr[9]);
        Number value = (Number) ittr[6];
        BigDecimal bdValue = value != null ? BigDecimal.valueOf(value.doubleValue()) : BigDecimal.ZERO;
        component.setValue(new Quantity()
                .setValue(bdValue)
                .setUnit((String) ittr[7])
                .setSystem("http://unitsofmeasure.org")
                .setCode((String) ittr[7]));
        return obs;
    }

    @Override
    public Bundle getPatientObservations(String subject,
                                         String code,
                                         String valueQuantity,
                                         String encounter,
                                         Pageable pageable) {
        if (ObjectUtils.isEmpty(subject) && ObjectUtils.isEmpty(code) && ObjectUtils.isEmpty(encounter)) {
            throw new MissingParametersException("Observation search params are missing at least provide code!");
        }
        ObservationDto dto = new ObservationDto();
        dto.setSubject(!ObjectUtils.isEmpty(subject) ? Integer.parseInt(subject) : null);
        dto.setEncounter(!ObjectUtils.isEmpty(encounter) ? Integer.parseInt(encounter) : null);
        dto.setCode(!ObjectUtils.isEmpty(code) ? code : "");
        FhirQuantity obj = !ObjectUtils.isEmpty(valueQuantity) ? FhirQuantity.parse(valueQuantity) : null;
        dto.setFhirQuantity(obj);

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.getMeta().setLastUpdated(new Date());
        List<Object[]> latestMeasurements = observationDao.setValueToNativeObservationQuery(dto);


        if (!ObjectUtils.isEmpty(latestMeasurements)) {
            for (Object[] ittr : latestMeasurements) {
                Observation obs = buildObservation(ittr);

                Bundle.BundleEntryComponent entry = bundle.addEntry();
                entry.setFullUrl("http://10.131.58.59:481/baseR4/Observation/" + obs.getId());
                entry.setResource(obs);
                entry.getSearch().setMode(Bundle.SearchEntryMode.MATCH);
            }
        }

        bundle.setTotal(latestMeasurements.size());

        bundle.addLink()
                .setRelation("self")
                .setUrl("http://10.131.58.59:481/baseR4/Observation?code=" + code + "&subject=" + subject);
        return bundle;
    }
}
