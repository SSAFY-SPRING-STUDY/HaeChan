package ssafy.study.ssafystudy.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ssafy.study.ssafystudy.auth.component.SessionManager;
import ssafy.study.ssafystudy.auth.util.AuthTokenUtils;
import ssafy.study.ssafystudy.member.controller.dto.MemberRequest;
import ssafy.study.ssafystudy.member.controller.dto.MemberResponse;
import ssafy.study.ssafystudy.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final SessionManager sessionManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse join(@RequestBody MemberRequest request) {
        return memberService.join(request);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse getMyInfo(@RequestParam String bearerToken) {
        if(AuthTokenUtils.isValidBearerToken(bearerToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 정보가 유효하지 않습니다.");
        }
        String sessionKey = AuthTokenUtils.parseBearerToken(bearerToken);

        Long memberId = sessionManager.getMemberId(sessionKey);

        return memberService.getMemberInfo(memberId);
    }
}
