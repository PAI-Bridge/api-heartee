package paibridge.apiheartee.counsel.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.conversation.entity.TempConversation;
import paibridge.apiheartee.conversation.repository.TempConversationRepository;
import paibridge.apiheartee.counsel.dto.CounselCreateDto;
import paibridge.apiheartee.counsel.dto.CounselDto;
import paibridge.apiheartee.counsel.dto.CounselPartnerCreateDto;
import paibridge.apiheartee.counsel.entity.CategoryType.Values;
import paibridge.apiheartee.counsel.entity.CounselReport;
import paibridge.apiheartee.counsel.entity.CounselRequest;
import paibridge.apiheartee.counsel.repository.CounselReportRepository;
import paibridge.apiheartee.counsel.repository.CounselRequestRepository;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselReportSaveOptionsDto;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselRequestDto;
import paibridge.apiheartee.counsel.service.gpt.GPTService;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.repository.MemberRepository;
import paibridge.apiheartee.partner.dto.PartnerCreateDto;
import paibridge.apiheartee.partner.entity.Partner;
import paibridge.apiheartee.partner.entity.PartnerBU;
import paibridge.apiheartee.partner.entity.PartnerDT;
import paibridge.apiheartee.partner.entity.PartnerGL;
import paibridge.apiheartee.partner.repository.PartnerRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselService {

    private final PartnerRepository partnerRepository;
    private final MemberRepository memberRepository;
    private final TempConversationRepository tempConversationRepository;
    private final CounselRequestRepository counselRequestRepository;
    private final CounselReportRepository counselReportRepository;
    private final GPTService gptService;

    @Transactional
    public Long createCounselAndPartner(Long memberId, CounselPartnerCreateDto dto)
        throws EntityNotFoundException {

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            throw new EntityNotFoundException("Member not found");
        }

        PartnerCreateDto partnerCreateDto = dto.getPartnerCreateDto();
        Partner partner = createPartner(member, partnerCreateDto);
        Partner savedPartner = partnerRepository.save(partner);

        TempConversation tempConversation = tempConversationRepository.findById(
            dto.getTempConversationId()).orElse(null);
        if (tempConversation == null) {
            throw new EntityNotFoundException("TempConversation not found");
        }

        CounselRequest counselRequest = createCounselRequest(savedPartner, tempConversation);
        CounselRequest savedCounselRequest = counselRequestRepository.save(counselRequest);

        ////////// GPT API CALL (Event Listener or Async) ////////////
        GPTCounselRequestDto req = GPTCounselRequestDto.builder()
                .language("ko")
                .userGender(member.getGender())
                .counterpartGender(savedPartner.getGender())
                .conversationHistory(tempConversation.getData().toString())
                .build();

        GPTCounselReportSaveOptionsDto reportSaveOptions = GPTCounselReportSaveOptionsDto.builder().dType(savedPartner.getDtype()).build();

        try {
            gptService.fetchCounselFromGPT(req, reportSaveOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////

        return savedCounselRequest.getId();
    }


    @Transactional
    public Long createCounsel(Long memberId, CounselCreateDto dto) throws EntityNotFoundException {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            throw new EntityNotFoundException("Member not found");
        }

        Partner partner = partnerRepository.findById(dto.getPartnerId()).orElse(null);
        if (partner == null) {
            throw new EntityNotFoundException("Partner not found");
        }

        TempConversation tempConversation = tempConversationRepository.findById(
            dto.getTempConversationId()).orElse(null);
        if (tempConversation == null) {
            throw new EntityNotFoundException("TempConversation not found");
        }

        CounselRequest counselRequest = createCounselRequest(partner, tempConversation);
        CounselRequest savedCounselRequest = counselRequestRepository.save(counselRequest);

        ////////// GPT API CALL (Event Listener or Async) ////////////

        //////////////////////////////////////////////////////////////

        return savedCounselRequest.getId();
    }

    private Partner createPartner(Member member, PartnerCreateDto dto) {

        if (dto.getDtype().equals(Values.GL)) {
            return PartnerGL.builder()
                .member(member)
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .age(dto.getAge())
                .mbti(dto.getMbti())
                .infoGL(dto.getInfoGL())
                .build();
        }

        if (dto.getDtype().equals(Values.DT)) {
            return PartnerDT.builder()
                .member(member)
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .age(dto.getAge())
                .mbti(dto.getMbti())
                .infoDT(dto.getInfoDT())
                .build();
        }

        if (dto.getDtype().equals(Values.BU)) {
            return PartnerBU.builder()
                .member(member)
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .age(dto.getAge())
                .mbti(dto.getMbti())
                .infoBU(dto.getInfoBU())
                .build();
        }

        throw new IllegalArgumentException("partner.dtype is invalid");
    }

    private CounselRequest createCounselRequest(Partner partner,
        TempConversation tempConversation) {

        return CounselRequest.builder()
            .partner(partner)
            .comment("예시 comment입니다")
            .price(123456789L)
            .build();
    }

    public CounselDto findCounsel(Long counselId) throws EntityNotFoundException {
        CounselRequest counselRequest = counselRequestRepository.findById(counselId).orElse(null);
        if (counselRequest == null) {
            throw new EntityNotFoundException("Counsel not found");
        }

        CounselReport counselReport = counselReportRepository.findByCounselRequestId(
            counselRequest.getId()).orElse(null);

        return CounselDto.create(counselRequest, counselReport);
    }

    public List<CounselDto> findCounsels(Long partnerId, Integer offset, Integer limit) {
        List<CounselRequest> counselRequests = counselRequestRepository.findByPartnerId(
            partnerId); // 동적 쿼리로 전환 필요

        List<CounselDto> dtos = new ArrayList<>();

        for (CounselRequest counselRequest : counselRequests) {
            CounselReport counselReport = counselReportRepository.findByCounselRequestId(
                counselRequest.getId()).orElse(null);

            dtos.add(CounselDto.create(counselRequest, counselReport));
        }

        return dtos;
    }
}
