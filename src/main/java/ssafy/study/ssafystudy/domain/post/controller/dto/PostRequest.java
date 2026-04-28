package ssafy.study.ssafystudy.domain.post.controller.dto;

import ssafy.study.ssafystudy.domain.post.entity.PostEntity;

public record PostRequest(
        String title,
        String content,
        String author) {

    public PostEntity toEntity() {
        return PostEntity.create(title, content, author);
    }
}
