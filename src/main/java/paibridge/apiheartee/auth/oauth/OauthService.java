package paibridge.apiheartee.auth.oauth;

import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import paibridge.apiheartee.member.dto.MemberOauthDto;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.entity.Role;
import paibridge.apiheartee.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(
            userRequest); // OAuth 서비스(kakao, google, naver)에서 가져온 유저 정보를 담고있음

        String registrationId = userRequest.getClientRegistration()
            .getRegistrationId(); // OAuth 서비스 이름(ex. kakao, naver, google)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName(); // OAuth 로그인 시 키(pk)가 되는 값 // "response"
        Map<String, Object> attributes = oAuth2User.getAttributes(); // OAuth 서비스의 유저 정보들

        MemberOauthDto dto = OauthAttributes.extract(registrationId,
            attributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
        Member member = saveOrUpdate(dto); // DB에 저장

        DefaultOAuth2User user = new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
            attributes, userNameAttributeName);
        return user;
    }

    private Member saveOrUpdate(MemberOauthDto dto) {

        Member foundMember = memberRepository.findByOauthId(dto.getOauthId()).orElse(null);

        if (foundMember == null) {
            Member newMember = Member.builder()
                .oauthType(dto.getOauthType())
                .oauthId(dto.getOauthId())
                .name(dto.getName())
                .email(dto.getEmail())
                .role(Role.USER)
                .build();

            return memberRepository.save(newMember);
        }

        foundMember.updateOauthInfo(dto.getName(), dto.getEmail());

        return memberRepository.save(foundMember);
    }
}