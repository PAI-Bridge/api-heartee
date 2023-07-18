package paibridge.apiheartee.partner.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import paibridge.apiheartee.partner.entity.Partner;

@RequiredArgsConstructor
public class CustomPartnerRepositoryImpl implements CustomPartnerRepository{

    private final EntityManager em;

    @Override
    public List<Partner> findAll(Long memberId, String dtype) {
        // 동적 쿼리 -> QueryDsl로 대체
        return em.createQuery("select p from Partner p", Partner.class)
            .getResultList();
    }
}
