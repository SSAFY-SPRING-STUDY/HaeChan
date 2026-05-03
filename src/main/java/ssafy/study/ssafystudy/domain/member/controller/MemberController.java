package ssafy.study.ssafystudy.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssafy.study.ssafystudy.domain.auth.component.SessionManager;
import ssafy.study.ssafystudy.domain.auth.util.AuthTokenUtils;
import ssafy.study.ssafystudy.domain.member.controller.dto.MemberRequest;
import ssafy.study.ssafystudy.domain.member.controller.dto.MemberResponse;
import ssafy.study.ssafystudy.domain.member.service.MemberService;
import ssafy.study.ssafystudy.global.exception.CustomException;
import ssafy.study.ssafystudy.global.exception.error.ErrorCode;

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
    public MemberResponse getMyInfo(@RequestHeader("Authorization") String bearerToken) {
        if(AuthTokenUtils.isValidBearerToken(bearerToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String sessionKey = AuthTokenUtils.parseBearerToken(bearerToken);

        Long memberId = sessionManager.getMemberId(sessionKey);

        return memberService.getMemberInfo(memberId);
    }
}
