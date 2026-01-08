package EHRAssist.utils;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class EHRAUtils {
    @Autowired
    private FhirContext fhirContext;

    public ResponseEntity<String> fhirResponseWrapper(Bundle response) {
        String json = fhirContext
                .newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(response);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/fhir+json; charset=UTF-8")
                .header(HttpHeaders.CONTENT_ENCODING, "identity")
                .body(json);
    }
}
