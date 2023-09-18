package paibridge.apiheartee;


import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.conversation.entity.TempConversation;
import paibridge.apiheartee.counsel.entity.CategoryType;
import paibridge.apiheartee.counsel.entity.CategoryType.Values;
import paibridge.apiheartee.counsel.entity.CounselCategory;
import paibridge.apiheartee.counsel.entity.CounselReport;
import paibridge.apiheartee.counsel.entity.CounselReportBU;
import paibridge.apiheartee.counsel.entity.CounselReportDT;
import paibridge.apiheartee.counsel.entity.CounselReportGL;
import paibridge.apiheartee.counsel.entity.CounselRequest;
import paibridge.apiheartee.counsel.repository.CounselRequestRepository;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.entity.OauthType;
import paibridge.apiheartee.member.repository.MemberRepository;
import paibridge.apiheartee.partner.entity.Mbti;
import paibridge.apiheartee.partner.entity.Partner;
import paibridge.apiheartee.partner.entity.PartnerBU;
import paibridge.apiheartee.partner.entity.PartnerDT;
import paibridge.apiheartee.partner.entity.PartnerGL;
import paibridge.apiheartee.partner.repository.PartnerRepository;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.categoryInit();
        initService.memberInit();
        initService.partnerInit();
//        initService.tempConversationInit();
        initService.counselRequestInit();
        initService.counselReportInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService {

        private final EntityManager em;
        private final MemberRepository memberRepository;
        private final PartnerRepository partnerRepository;
        private final CounselRequestRepository counselRequestRepository;

        public void categoryInit() {
            CounselCategory gl = CounselCategory.builder()
                .code(CategoryType.GL)
                .title("그린라이트 신호등")
                .subtitle("붉은색 푸른색 그 사이 3초만에 판단해드립니다.")
                .imageUrl("test.jpg")
                .build();

            CounselCategory dt = CounselCategory.builder()
                .code(CategoryType.DT)
                .title("오빠는 뭐가 문젠지 모르겠어?")
                .subtitle("긴급상황 발생! 해결책을 찾아드립니다.")
                .imageUrl("test.jpg")
                .build();

            CounselCategory bu = CounselCategory.builder()
                .code(CategoryType.BU)
                .title("우리 다시 만날 수 있을까?")
                .subtitle("헤어진 연인이 아직 생각난다면?")
                .imageUrl("test.jpg")
                .build();

            em.persist(gl);
            em.persist(dt);
            em.persist(bu);
        }

        public void memberInit() {
            Member member1 = Member.builder()
                .oauthType(OauthType.GOOGLE)
                .oauthId("minyongId")
                .email("minyong@gmail.com")
                .name("이민용")
                .nickname("민용짱")
                .gender("male")
                .age(26)
                .build();

            Member member2 = Member.builder()
                .oauthType(OauthType.NAVER)
                .oauthId("dongminId")
                .email("dongmin@gmail.com")
                .name("김동민")
                .nickname("동민짱짱")
                .gender("male")
                .age(26)
                .build();

            em.persist(member1);
            em.persist(member2);
        }

        public void partnerInit() {
            Member member1 = memberRepository.findByName("이민용").orElse(null);
            Member member2 = memberRepository.findByName("김동민").orElse(null);

            Partner partner1 = PartnerGL.builder()
                .member(member1)
                .nickname("민용썸녀")
                .gender("female")
                .age(25)
                .mbti(Mbti.ENFP)
                .infoGL("타입 GL에 대한 질문의 응답")
                .build();

            Partner partner2 = PartnerDT.builder()
                .member(member1)
                .nickname("민용여친")
                .gender("female")
                .age(25)
                .mbti(Mbti.INFJ)
                .infoDT("타입 DT에 대한 질문의 응답")
                .build();

            Partner partner3 = PartnerBU.builder()
                .member(member2)
                .nickname("동민전여친")
                .gender("female")
                .age(25)
                .mbti(Mbti.ESFP)
                .infoBU("타입 BU에 대한 질문의 응답")
                .build();

            em.persist(partner1);
            em.persist(partner2);
            em.persist(partner3);
        }

//        public void tempConversationInit() {
//            JSONObject obj1 = new JSONObject();
//            obj1.put("a", "asdf");
//            obj1.put("b", 123);
//            obj1.put("3", new JSONObject());
//
//            TempConversation tempConversation1 = TempConversation.builder()
//                .price(1000)
//                .data(obj1)
//                .build();
//
//            em.persist(tempConversation1);
//
//            JSONObject obj2 = new JSONObject();
//            obj2.put("a", "asdf");
//            obj2.put("b", 123);
//            obj2.put("3", new JSONObject());
//
//            TempConversation tempConversation2 = TempConversation.builder()
//                .price(500)
//                .data(obj2)
//                .build();
//
//            em.persist(tempConversation2);
//        }

        public void counselRequestInit() {
            Partner partner = partnerRepository.findById(6L).orElse(null);

            CounselRequest counselRequest1 = CounselRequest.builder()
                .partner(partner)
                .price(600L)
                .comment("추가 설명 예시입니다. 아직 counselReport가 생성되지 않은 counselRequest 샘플입니다.")
                .build();

            CounselRequest counselRequest2 = CounselRequest.builder()
                .partner(partner)
                .price(400L)
                .comment("추가 설명 예시입니다. counselReport가 생성된 counselRequest 샘플입니다.")
                .build();

            em.persist(counselRequest1);
            em.persist(counselRequest2);


        }

        public void counselReportInit() {
            Partner partner = partnerRepository.findById(6L).orElse(null);
            assert partner != null;

            CounselRequest counselRequest = counselRequestRepository.findById(12L).orElse(null);

            String dtype = partner.getDtype();
            CounselReport counselReport = null;
            if (dtype.equals(Values.GL)) {
                counselReport = CounselReportGL.builder()
                    .counselRequest(counselRequest)
                        .summary("summary 샘플입니다.")
                    .solution("solution 샘플입니다.")
                        .willingness(8)
                        .selfOpenness(2)
                        .voiceOver(4)
                        .positiveLanguage(6)
                        .frequency(2)
                        .explanation("explanation 샘플입니다.")
                    .build();

            } else if (dtype.equals(Values.DT)) {
                counselReport = CounselReportDT.builder()
                    .counselRequest(counselRequest)
                    .summary("summary 샘플입니다.")
                    .solution("solution 샘플입니다.")
                        .willingness(8)
                        .selfOpenness(2)
                        .voiceOver(4)
                        .positiveLanguage(6)
                        .frequency(2)
                    .build();

            } else if (dtype.equals(Values.BU)) {
                counselReport = CounselReportBU.builder()
                    .counselRequest(counselRequest)
                    .summary("summary 샘플입니다.")
                    .solution("solution 샘플입니다.")
                        .willingness(8)
                        .selfOpenness(2)
                        .voiceOver(4)
                        .positiveLanguage(6)
                        .frequency(2)
                    .build();
            }
            em.persist(counselReport);
        }
    }
}
