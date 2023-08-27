package paibridge.apiheartee.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import paibridge.apiheartee.member.entity.OauthType;

@RequiredArgsConstructor
@Getter
public class MemberOauthDto {

    private final OauthType oauthType;
    private final String oauthId;
    private final String name;
    private final String email;
}
