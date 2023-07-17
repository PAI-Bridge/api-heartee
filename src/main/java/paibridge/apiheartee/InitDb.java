package paibridge.apiheartee;


import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.entity.OauthType;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit() {
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
    }
}
