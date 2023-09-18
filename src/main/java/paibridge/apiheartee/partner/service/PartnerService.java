package paibridge.apiheartee.partner.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.counsel.entity.CategoryType;
import paibridge.apiheartee.counsel.entity.CounselCategory;
import paibridge.apiheartee.counsel.repository.CounselCategoryRepository;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.partner.dto.PartnerCreateDto;
import paibridge.apiheartee.partner.dto.PartnerDto;
import paibridge.apiheartee.partner.entity.Partner;
import paibridge.apiheartee.partner.entity.PartnerBU;
import paibridge.apiheartee.partner.entity.PartnerDT;
import paibridge.apiheartee.partner.entity.PartnerGL;
import paibridge.apiheartee.partner.repository.PartnerRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final EntityManager em;
    private final CounselCategoryRepository counselCategoryRepository;


    public List<PartnerDto> findPartners(Long memberId, String dtype) {

        List<CounselCategory> categories = counselCategoryRepository.findAll();
        List<Partner> partners = partnerRepository.findAll(memberId, dtype);
        return partners.stream()
            .map(p -> PartnerDto.create(p, categories))
            .collect(Collectors.toList());
    }

    public PartnerDto findPartner(Long partnerId) throws EntityNotFoundException {

        List<CounselCategory> categories = counselCategoryRepository.findAll();
        Partner partner = partnerRepository.findById(partnerId).orElse(null);

        if (partner == null) {
            throw new EntityNotFoundException("Partner not found");
        }

        return PartnerDto.create(partner, categories);
    }

    public Partner createPartner(Member member, PartnerCreateDto dto) throws IllegalArgumentException {
        if (dto.getDType().equals(CategoryType.Values.GL)) {
            PartnerGL partnerGL = PartnerGL.builder()
                    .member(member)
                    .nickname(dto.getNickname())
                    .gender(dto.getGender())
                    .age(dto.getAge())
                    .mbti(dto.getMbti())
                    .infoGL(dto.getInfoGL())
                    .build();
            PartnerGL savedPartner = partnerRepository.save(partnerGL);
            em.flush();
            em.clear();
            return savedPartner;
        }

        if (dto.getDType().equals(CategoryType.Values.DT)) {
            PartnerDT partnerDT = PartnerDT.builder()
                    .member(member)
                    .nickname(dto.getNickname())
                    .gender(dto.getGender())
                    .age(dto.getAge())
                    .mbti(dto.getMbti())
                    .infoDT(dto.getInfoDT())
                    .build();
            PartnerDT savedPartner = partnerRepository.save(partnerDT);
            em.flush();
            em.clear();
            return savedPartner;
        }

        if (dto.getDType().equals(CategoryType.Values.BU)) {
            PartnerBU partnerBU = PartnerBU.builder()
                    .member(member)
                    .nickname(dto.getNickname())
                    .gender(dto.getGender())
                    .age(dto.getAge())
                    .mbti(dto.getMbti())
                    .infoBU(dto.getInfoBU())
                    .build();
            PartnerBU savedPartner = partnerRepository.save(partnerBU);
            em.flush();
            em.clear();
            return savedPartner;
        }

        throw new IllegalArgumentException("partner.dtype is invalid");
    }
}
