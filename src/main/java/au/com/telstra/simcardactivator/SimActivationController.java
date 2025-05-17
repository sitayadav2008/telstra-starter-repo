package au.com.telstra.simcardactivator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/sim")
public class SimActivationController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/activate")
    public ResponseEntity<String> activateSim(@RequestBody SimActivationRequest request) {
        String actuatorUrl = "http://localhost:8444/actuate";
        ActuatorRequest actuatorRequest = new ActuatorRequest(request.getIccid());

        ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(
                actuatorUrl, actuatorRequest, ActuatorResponse.class);

        if (response.getBody() != null && response.getBody().isSuccess()) {
            System.out.println("Activation successful for ICCID: " + request.getIccid());
            return ResponseEntity.ok("Activation successful");
        } else {
            System.out.println("Activation failed for ICCID: " + request.getIccid());
            return ResponseEntity.status(500).body("Activation failed");
        }
    }

    public static class SimActivationRequest {
        private String iccid;
        private String customerEmail;

        public String getIccid() { return iccid; }
        public void setIccid(String iccid) { this.iccid = iccid; }

        public String getCustomerEmail() { return customerEmail; }
        public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    }

    public static class ActuatorRequest {
        private String iccid;

        public ActuatorRequest() {}
        public ActuatorRequest(String iccid) { this.iccid = iccid; }

        public String getIccid() { return iccid; }
        public void setIccid(String iccid) { this.iccid = iccid; }
    }

    public static class ActuatorResponse {
        private boolean success;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }
}
