package paibridge.apiheartee.member.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
