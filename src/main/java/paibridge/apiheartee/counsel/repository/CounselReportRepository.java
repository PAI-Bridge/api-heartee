package paibridge.apiheartee.counsel.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paibridge.apiheartee.counsel.entity.CounselReport;

@Repository
public interface CounselReportRepository extends JpaRepository<CounselReport, Long> {

    Optional<CounselReport> findByCounselRequestId(Long counselRequestId);
}
