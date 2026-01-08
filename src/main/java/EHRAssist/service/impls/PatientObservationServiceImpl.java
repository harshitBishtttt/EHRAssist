package EHRAssist.service.impls;

import EHRAssist.repository.PersonMeasurementRepository;
import EHRAssist.service.PatientObservationService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PatientObservationServiceImpl implements PatientObservationService {

    @Autowired
    private PersonMeasurementRepository personMeasurementRepository;

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
        component.setValue(new Quantity()
                .setValue(((Number) ittr[6]).doubleValue())
                .setUnit((String) ittr[7])
                .setSystem("http://unitsofmeasure.org")
                .setCode((String) ittr[7]));
        return obs;
    }

    @Override
    public Bundle getPatientObservations(Integer subject, String code, Integer encounter, Pageable pageable) {

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setId(subject.toString());
        bundle.getMeta().setLastUpdated(new Date());

        List<Object[]> latestMeasurements =
                personMeasurementRepository.findLatestMeasurements(subject, code, encounter);

        for (Object[] ittr : latestMeasurements) {
            Observation obs = buildObservation(ittr);

            Bundle.BundleEntryComponent entry = bundle.addEntry();
            entry.setFullUrl("http://10.131.58.59:481/baseR4/Observation/" + obs.getId());
            entry.setResource(obs);
            entry.getSearch().setMode(Bundle.SearchEntryMode.MATCH);
        }

        bundle.setTotal(latestMeasurements.size());

        bundle.addLink()
                .setRelation("self")
                .setUrl("http://10.131.58.59:481/baseR4/Observation?code=" + code + "&subject=" + subject);
        return bundle;
    }
}
