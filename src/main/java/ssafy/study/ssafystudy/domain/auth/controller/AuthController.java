package ssafy.study.ssafystudy.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssafy.study.ssafystudy.domain.auth.controller.dto.LoginRequest;
import ssafy.study.ssafystudy.domain.auth.controller.dto.LoginResponse;
import ssafy.study.ssafystudy.domain.auth.service.AuthService;
import ssafy.study.ssafystudy.domain.auth.util.AuthTokenUtils;
import ssafy.study.ssafystudy.global.exception.CustomException;
import ssafy.study.ssafystudy.global.exception.error.ErrorCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String bearerToken) {
        if(AuthTokenUtils.isValidBearerToken(bearerToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String sessionKey = AuthTokenUtils.parseBearerToken(bearerToken);
        authService.logout(sessionKey);
    }
}
