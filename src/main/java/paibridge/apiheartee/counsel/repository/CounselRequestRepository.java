package paibridge.apiheartee.counsel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import paibridge.apiheartee.counsel.entity.CounselRequest;

public interface CounselRequestRepository extends JpaRepository<CounselRequest, Long> {

    List<CounselRequest> findByPartnerId(Long partnerId);
}
