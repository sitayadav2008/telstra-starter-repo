package au.com.telstra.simcardactivator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity  // ✅ Add this annotation
@Table(name = "sim_card_activations")
public class SimCardActivationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iccid;
    private String customerEmail;
    private boolean activationStatus;
    private LocalDateTime timestamp;

    public SimCardActivationRecord() {}

    public SimCardActivationRecord(String iccid, String customerEmail, boolean activationStatus) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
        this.activationStatus = activationStatus;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getIccid() { return iccid; }
    public void setIccid(String iccid) { this.iccid = iccid; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public boolean isActivationStatus() { return activationStatus; }
    public void setActivationStatus(boolean activationStatus) { this.activationStatus = activationStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
