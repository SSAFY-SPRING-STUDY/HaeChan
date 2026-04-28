package ssafy.study.ssafystudy.domain.post.controller.dto;

import ssafy.study.ssafystudy.domain.post.entity.PostEntity;

public record PostResponse(
        Long id,
        String title,
        String content,
        String author
) {
    public static PostResponse fromEntity(PostEntity savedPost) {
        return new PostResponse(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(), savedPost.getAuthor());
    }
}
