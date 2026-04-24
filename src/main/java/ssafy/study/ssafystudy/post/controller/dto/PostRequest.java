package ssafy.study.ssafystudy.post.controller.dto;

import ssafy.study.ssafystudy.post.entity.PostEntity;

public record PostRequest(
        String title,
        String content,
        String author) {

    public PostEntity toEntity() {
        return PostEntity.create(title, content, author);
    }
}
