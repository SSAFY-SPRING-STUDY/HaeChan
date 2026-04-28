package ssafy.study.ssafystudy.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssafy.study.ssafystudy.domain.auth.component.SessionManager;
import ssafy.study.ssafystudy.domain.auth.controller.dto.LoginRequest;
import ssafy.study.ssafystudy.domain.auth.controller.dto.LoginResponse;
import ssafy.study.ssafystudy.domain.member.entity.MemberEntity;
import ssafy.study.ssafystudy.domain.member.repository.MemberRepository;
import ssafy.study.ssafystudy.global.exception.CustomException;
import ssafy.study.ssafystudy.global.exception.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionManager sessionManager;
    private final MemberRepository memberRepository;

    public LoginResponse login(LoginRequest request) {
        MemberEntity member = memberRepository.findByUsername(request.username())
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_USERNAME));
        if(!member.checkPassword(request.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String sessionKey = sessionManager.createSession(member.getId());

        return LoginResponse.from(sessionKey);
    }

    public void logout(String sessionKey) {
        sessionManager.removeSession(sessionKey);
    }
}
