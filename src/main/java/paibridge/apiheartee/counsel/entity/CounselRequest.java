package paibridge.apiheartee.counsel.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import paibridge.apiheartee.common.entity.BaseEntity;
import paibridge.apiheartee.conversation.entity.Conversation;
import paibridge.apiheartee.partner.entity.Partner;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CounselRequest extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "counsel_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToOne(mappedBy = "counselRequest")
    private CounselReport counselReport;

    @OneToMany(mappedBy = "counselRequest")
    private List<Conversation> conversations;

    private String comment;
    private Long price;

    @Builder
    public CounselRequest(Partner partner, String comment, Long price) {
        this.partner = partner;
        this.comment = comment;
        this.price = price;
    }
}
