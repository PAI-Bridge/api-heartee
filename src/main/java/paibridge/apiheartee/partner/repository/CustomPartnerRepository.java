package paibridge.apiheartee.partner.repository;

import java.util.List;
import paibridge.apiheartee.partner.entity.Partner;

public interface CustomPartnerRepository {

    List<Partner> findAll(Long memberId, String dtype);

}
