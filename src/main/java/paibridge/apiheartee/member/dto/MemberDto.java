package paibridge.apiheartee.member.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.member.entity.Member;

@Data
@Builder
public class MemberDto {

    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String gender;
    private Integer age;

    public static MemberDto create(Member entity) {
        return MemberDto.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .name(entity.getName())
            .nickname(entity.getNickname())
            .gender(entity.getGender())
            .age(entity.getAge())
            .build();
    }
}
