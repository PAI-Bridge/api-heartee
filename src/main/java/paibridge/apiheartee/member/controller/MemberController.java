package paibridge.apiheartee.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.common.dto.DataArrayResponse;
import paibridge.apiheartee.member.dto.MemberDto;
import paibridge.apiheartee.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public DataArrayResponse<MemberDto> findMembers() {
        List<MemberDto> members = memberService.findMembers();

        return new DataArrayResponse<>(members);
    }
}
