package ssafy.study.ssafystudy.domain.post.entity;

import lombok.Getter;
import ssafy.study.ssafystudy.domain.member.entity.MemberEntity;

@Getter
public class PostEntity {
    private static long AUTO_INCREMENT = 1;

    private Long id;
    private String title;
    private String content;
    private MemberEntity author;

    private PostEntity(String title, String content, MemberEntity author) {
        this.id = AUTO_INCREMENT++;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostEntity create(String title, String content, MemberEntity author) {
        return new PostEntity(title, content, author);
    }
}
