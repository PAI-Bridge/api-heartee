package paibridge.apiheartee.member.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.member.dto.MemberDto;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberDto> findMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
            .map(MemberDto::create)
            .collect(Collectors.toList());
    }
}
