package ssafy.study.ssafystudy.entity;

import lombok.Getter;

@Getter
public class PostEntity {
    private static long AUTO_INCREMENT = 1;

    private Long id;
    private String title;
    private String content;
    private String author;

    private PostEntity(String title, String content, String author) {
        this.id = AUTO_INCREMENT++;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static PostEntity create(String title, String content, String author) {
        return new PostEntity(title, content, author);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
