package ssafy.study.ssafystudy.domain.auth.component;

import org.springframework.stereotype.Component;
import ssafy.study.ssafystudy.global.exception.CustomException;
import ssafy.study.ssafystudy.global.exception.error.ErrorCode;

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
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return sessionStore.get(sessionKey);
    }
}
