package paibridge.apiheartee.counsel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import paibridge.apiheartee.common.entity.BaseEntity;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CounselCategory extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "counsel_category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryType code;

    private String title;
    private String subtitle;
    private String imageUrl;

    @Builder
    public CounselCategory(CategoryType code, String title, String subtitle, String imageUrl) {
        this.code = code;
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
    }
}
