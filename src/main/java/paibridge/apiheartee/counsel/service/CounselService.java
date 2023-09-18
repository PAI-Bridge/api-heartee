package paibridge.apiheartee.counsel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.conversation.entity.TempConversation;
import paibridge.apiheartee.conversation.repository.TempConversationRepository;
import paibridge.apiheartee.conversation.service.image.dto.Author;
import paibridge.apiheartee.counsel.dto.CounselCreateDto;
import paibridge.apiheartee.counsel.dto.CounselDto;
import paibridge.apiheartee.counsel.dto.CounselPartnerCreateDto;
import paibridge.apiheartee.counsel.entity.CounselReport;
import paibridge.apiheartee.counsel.entity.CounselRequest;
import paibridge.apiheartee.counsel.repository.CounselReportRepository;
import paibridge.apiheartee.counsel.repository.CounselRequestRepository;
import paibridge.apiheartee.counsel.service.gpt.dto.ChatForGPTRequestDto;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselReportSaveOptionsDto;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselRequestDto;
import paibridge.apiheartee.counsel.service.gpt.GPTService;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.repository.MemberRepository;
import paibridge.apiheartee.partner.dto.PartnerCreateDto;
import paibridge.apiheartee.partner.entity.Partner;
import paibridge.apiheartee.partner.repository.PartnerRepository;
import paibridge.apiheartee.partner.service.PartnerService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselService {

    private final PartnerService partnerService;
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
        Partner savedPartner = partnerService.createPartner(member, partnerCreateDto);

        TempConversation tempConversation = tempConversationRepository.findById(
            dto.getTempConversationId()).orElse(null);
        if (tempConversation == null) {
            throw new EntityNotFoundException("TempConversation not found");
        }

        CounselRequest counselRequest = createCounselRequest(savedPartner, tempConversation);

        CounselRequest savedCounselRequest = counselRequestRepository.save(counselRequest);

        ////////// GPT API CALL (Event Listener or Async) ////////////
        // FIXME : 텍스트 추출 단계에서 partner의 이름을 별도로 인식할 방법이 마땅히 생각이 안남 ㅠ
        // FIXME : partner의 nickname을 카톡에 표시되는 이름과 동일, 혹은 유사하게 작성한다는 전제 하에, 호출시 이를 filtering하는 방식으로 구현
        saveGPTCounsel(savedCounselRequest, tempConversation, member, savedPartner);

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
        saveGPTCounsel(savedCounselRequest, tempConversation, member, partner);
        //////////////////////////////////////////////////////////////

        return savedCounselRequest.getId();
    }

    private void saveGPTCounsel(CounselRequest counselRequest, TempConversation tempConversation, Member member, Partner partner) {
        List<ChatForGPTRequestDto> chatsNicknameFiltered = tempConversation.getData().stream().filter(chat -> !chat.getContent().equals(partner.getNickname())).map(chat -> {
            if (chat.getAuthor() == Author.partner) {
                return ChatForGPTRequestDto.builder()
                        .author(partner.getNickname())
                        .content(chat.getContent())
                        .time(chat.getTime())
                        .build();
            }
            return ChatForGPTRequestDto.builder()
                    .author("user")
                    .content(chat.getContent())
                    .time(chat.getTime())
                    .build();

        }).collect(Collectors.toList());

        String conversationHistoryString = buildConversationHistory(chatsNicknameFiltered);
        GPTCounselRequestDto req = GPTCounselRequestDto.builder()
                .language("Korean")
                .user_gender(member.getGender())
                .counterpart_gender(partner.getGender())
                .conversation_history(conversationHistoryString)
                .build();

        GPTCounselReportSaveOptionsDto reportSaveOptions = GPTCounselReportSaveOptionsDto.builder()
                .dType(partner.getDtype())
                .counselRequest(counselRequest)
                .build();

        try {
            gptService.fetchCounsel(req, reportSaveOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildConversationHistory(List<ChatForGPTRequestDto> chatsForGPTRequestDtos) {
        String BACKTICK = "\\";
        String QUOTATION = "\"";

        List<String> chats = chatsForGPTRequestDtos.stream().map(chat -> {
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder stringBuilt = stringBuilder
                    .append("{author: ")
                    .append(BACKTICK)
                    .append(QUOTATION)
                    .append(chat.getAuthor())
                    .append(BACKTICK)
                    .append(QUOTATION)
                    .append(", ")
                    .append("content:")
                    .append(BACKTICK)
                    .append(QUOTATION)
                    .append(chat.getContent())
                    .append(BACKTICK)
                    .append(QUOTATION)
                    .append(", ")
                    .append("time:")
                    .append(BACKTICK)
                    .append(QUOTATION)
                    .append(chat.getTime())
                    .append(BACKTICK)
                    .append(QUOTATION)
                    .append("}");
            return stringBuilt.toString();
        }).collect(Collectors.toList());

        return "[" + String.join(", ", chats) + "]";
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
