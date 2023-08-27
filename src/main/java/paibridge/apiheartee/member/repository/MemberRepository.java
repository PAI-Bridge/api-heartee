package paibridge.apiheartee.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paibridge.apiheartee.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    Optional<Member> findByOauthId(String oauthId);
}
