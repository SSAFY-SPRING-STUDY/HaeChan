package ssafy.study.ssafystudy.auth.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTokenUtils {
    private static final String PREFIX_BEARER = "Bearer ";

    public static boolean isValidBearerToken(String bearerToken) {
        return bearerToken == null || !bearerToken.startsWith(PREFIX_BEARER);
    }

    public static String parseBearerToken(String bearerToken) {
        return bearerToken.substring(PREFIX_BEARER.length());
    }
}
