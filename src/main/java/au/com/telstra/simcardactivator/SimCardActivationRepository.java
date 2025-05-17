package au.com.telstra.simcardactivator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SimCardActivationRepository extends JpaRepository<SimCardActivationRecord, Long> {
}
