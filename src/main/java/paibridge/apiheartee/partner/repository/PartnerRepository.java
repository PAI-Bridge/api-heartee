package paibridge.apiheartee.partner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paibridge.apiheartee.partner.entity.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long>, CustomPartnerRepository {

}
