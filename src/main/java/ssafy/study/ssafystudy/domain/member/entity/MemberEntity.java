package ssafy.study.ssafystudy.domain.member.entity;

import lombok.Getter;

@Getter
public class MemberEntity {
    private static long AUTO_INCREMENT = 1L;

    private Long id;
    private String username;
    private String password;
    private String nickname;

    private MemberEntity(Long id, String username, String password, String nickname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }


    public static MemberEntity create(String username, String password, String nickname) {
        return new MemberEntity(AUTO_INCREMENT++, username, password, nickname);
    }
}
