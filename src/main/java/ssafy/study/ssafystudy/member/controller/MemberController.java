package ssafy.study.ssafystudy.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssafy.study.ssafystudy.member.controller.dto.MemberRequest;
import ssafy.study.ssafystudy.member.controller.dto.MemberResponse;
import ssafy.study.ssafystudy.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse join(@RequestBody MemberRequest request) {
        return memberService.join(request);
    }
}
