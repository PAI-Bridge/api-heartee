package paibridge.apiheartee.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.auth.jwt.JwtProvider;
import paibridge.apiheartee.member.dto.MemberDto;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public List<MemberDto> findMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
            .map(MemberDto::create)
            .collect(Collectors.toList());
    }

    public Long findMemberIdByOauthId(String oauthId) {
        log.info("oauthId = {}", oauthId);

        Optional<Member> member = memberRepository.findByOauthId(oauthId);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("Member not found");
        }

        return member.get().getId();
    }
}
