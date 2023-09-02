package paibridge.apiheartee.auth.oauth;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import paibridge.apiheartee.member.dto.MemberOauthDto;
import paibridge.apiheartee.member.entity.OauthType;


@Slf4j
public enum OauthAttributes {
    KAKAO("kakao", (attributes) -> {
        log.info(attributes.toString());
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        return new MemberOauthDto(
            OauthType.KAKAO,
            String.valueOf(attributes.get("id")),
            (String) properties.get("nickname"),
            (String) account.get("email")
        );
    }),
    GOOGLE("google", (attributes) -> {
        return new MemberOauthDto(
            OauthType.GOOGLE,
            String.valueOf(attributes.get("sub")),
            (String) attributes.get("name"),
            (String) attributes.get("email")
        );
    }),
    NAVER("naver", (attributes) -> {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return new MemberOauthDto(
            OauthType.NAVER,
            (String) response.get("id"),
            (String) response.get("name"),
            (String) response.get("email")
        );
    });

    private final String registrationId;
    private final Function<Map<String, Object>, MemberOauthDto> of;

    OauthAttributes(String registrationId, Function<Map<String, Object>, MemberOauthDto> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static MemberOauthDto extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
            .filter(provider -> registrationId.equals(provider.registrationId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new)
            .of.apply(attributes);
    }
}
