package ssafy.study.ssafystudy.controller.dto;

import ssafy.study.ssafystudy.entity.PostEntity;

public record PostRequest(
        String title,
        String content,
        String author) {

    public PostEntity toEntity() {
        return PostEntity.create(title, content, author);
    }
}
