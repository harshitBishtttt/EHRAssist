package EHRAssist.controller;


import EHRAssist.dto.request.LoginRequest;
import EHRAssist.exceptionHandler.exceptions.InvalidFirebaseCredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Value("${firebase.api-key}")
    private String fireBaseApiKey;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                + fireBaseApiKey;

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("email", request.getEmail());
        body.put("password", request.getPassword());
        body.put("returnSecureToken", true);
        ResponseEntity<String> response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            response =
                    restTemplate.postForEntity(url, entity, String.class);
        } catch (RuntimeException ex) {
            throw new InvalidFirebaseCredException("Incorrect Credentials!");
        }
        return response;
    }
}
