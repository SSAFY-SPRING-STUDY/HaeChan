package ssafy.study.ssafystudy.domain.auth.component;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private static final ConcurrentHashMap<String, Long> sessionStore = new ConcurrentHashMap<>();

    public String createSession(Long memberId) {
        String sessionKey = UUID.randomUUID().toString();
        sessionStore.put(sessionKey, memberId);
        return sessionKey;
    }

    public void removeSession(String sessionKey) {
        sessionStore.remove(sessionKey);
    }

    public Long getMemberId(String sessionKey) {
        if(sessionKey == null || !sessionStore.containsKey(sessionKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인한 회원이 아닙니다.");
        }
        return sessionStore.get(sessionKey);
    }
}
