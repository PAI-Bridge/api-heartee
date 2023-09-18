package paibridge.apiheartee.conversation.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import paibridge.apiheartee.common.entity.BaseEntity;
import paibridge.apiheartee.conversation.service.image.dto.ChatDto;
import paibridge.apiheartee.conversation.service.image.dto.ConcattedChatWithVertexAndTime;

import java.util.ArrayList;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class TempConversation extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "temp_conversation_id")
    private Long id;

    private Integer price;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private ArrayList<ChatDto> data;

    @Builder
    public TempConversation(Integer price, ArrayList<ChatDto> data) {
        this.price = price;
        this.data = data;
    }
}
