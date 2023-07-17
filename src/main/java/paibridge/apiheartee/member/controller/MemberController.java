package paibridge.apiheartee.member.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.common.dto.BodyArrayDto;
import paibridge.apiheartee.member.dto.MemberDto;
import paibridge.apiheartee.member.entity.Member;
import paibridge.apiheartee.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @RequestMapping
    public BodyArrayDto<MemberDto> findMembers() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> responseBody = new ArrayList<>();

        for (Member member : members) {
            responseBody.add(MemberDto.toDto(member));
        }

        return new BodyArrayDto<>(responseBody);
    }
}
