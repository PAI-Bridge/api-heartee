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
import paibridge.apiheartee.counsel.entity.CounselCategory;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.entity.OauthType;
import paibridge.apiheartee.member.repository.MemberRepository;
import paibridge.apiheartee.partner.entity.Mbti;
import paibridge.apiheartee.partner.entity.Partner;
import paibridge.apiheartee.partner.entity.PartnerBU;
import paibridge.apiheartee.partner.entity.PartnerDT;
import paibridge.apiheartee.partner.entity.PartnerGL;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.categoryInit();
        initService.memberInit();
        initService.partnerInit();
        initService.tempConversationInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService{

        private final EntityManager em;
        private final MemberRepository memberRepository;

        public void categoryInit() {
            CounselCategory gl = CounselCategory.builder()
                .code(CategoryType.GL)
                .title("그린라이트 신호등")
                .subtitle("붉은색 푸른색 그 사이 3초만에 판단해드립니다.")
                .build();

            CounselCategory dt = CounselCategory.builder()
                .code(CategoryType.DT)
                .title("오빠는 뭐가 문젠지 모르겠어?")
                .subtitle("긴급상황 발생! 해결책을 찾아드립니다.")
                .build();

            CounselCategory bu = CounselCategory.builder()
                .code(CategoryType.BU)
                .title("우리 다시 만날 수 있을까?")
                .subtitle("헤어진 연인이 아직 생각난다면?")
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
                .info("타입 GL에 대한 질문의 응답")
                .build();

            PartnerDT partner2 = PartnerDT.builder()
                .member(member1)
                .nickname("민용여친")
                .gender("female")
                .age(25)
                .mbti(Mbti.INFJ)
                .info("타입 DT에 대한 질문의 응답")
                .build();

            PartnerBU partner3 = PartnerBU.builder()
                .member(member2)
                .nickname("동민전여친")
                .gender("female")
                .age(25)
                .mbti(Mbti.ESFP)
                .info("타입 BU에 대한 질문의 응답")
                .build();

            em.persist(partner1);
            em.persist(partner2);
            em.persist(partner3);
        }

        public void tempConversationInit() {
            JSONObject obj = new JSONObject();
            obj.put("a", "asdf");
            obj.put("b", 123);
            obj.put("3", new JSONObject());

            TempConversation tempConversation = TempConversation.builder()
                .price(1000)
                .data(obj)
                .build();

            em.persist(tempConversation);
        }
    }
}
