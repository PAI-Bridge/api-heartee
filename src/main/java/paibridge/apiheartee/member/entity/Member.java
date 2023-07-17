package paibridge.apiheartee.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import paibridge.apiheartee.common.entity.BaseEntity;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    // OAuth2.0 인증 관련 필드
    @Enumerated(EnumType.STRING)
    private OauthType oauthType;
    private String oauthId;
    private String email;
    private String name;

    private String nickname;
    private String gender;
    private Integer age;
}
