package au.com.telstra.simcardactivator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class SimCardController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ACTUATOR_URL = "http://localhost:8444/actuate";

    @Autowired
    private SimCardActivationRepository repository;

    @PostMapping("/activate")
    public ResponseEntity<String> activateSimCard(@RequestBody SimCardRequest request) {
        // Prepare actuator request body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = String.format("{\"iccid\":\"%s\"}", request.getIccid());
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(
                    ACTUATOR_URL, entity, ActuatorResponse.class
            );

            boolean activationSuccess = response.getBody() != null && response.getBody().isSuccess();

            // Log result
            if (activationSuccess) {
                System.out.println("SIM Activation Success for ICCID: " + request.getIccid());
            } else {
                System.out.println("SIM Activation Failed for ICCID: " + request.getIccid());
            }

            // Save to database
            SimCardActivationRecord record = new SimCardActivationRecord(
                    request.getIccid(),
                    request.getCustomerEmail(),
                    activationSuccess
            );
            repository.save(record);

            // Return response
            if (activationSuccess) {
                return ResponseEntity.ok("SIM activation successful");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SIM activation failed");
            }

        } catch (Exception e) {
            System.out.println("Error calling actuator: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }
}
