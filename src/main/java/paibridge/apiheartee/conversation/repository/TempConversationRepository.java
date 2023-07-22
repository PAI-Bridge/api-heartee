package paibridge.apiheartee.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paibridge.apiheartee.conversation.entity.TempConversation;

public interface TempConversationRepository extends JpaRepository<TempConversation, Long> {

}
