package ssafy.study.ssafystudy.domain.auth.controller.dto;

public record LoginRequest(
        String username,
        String password
) {
}
