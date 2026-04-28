package ssafy.study.ssafystudy.domain.post.controller.dto;

import ssafy.study.ssafystudy.domain.member.controller.dto.MemberResponse;
import ssafy.study.ssafystudy.domain.post.entity.PostEntity;

public record PostResponse(
        Long id,
        String title,
        String content,
        MemberResponse memberResponse
) {
    public static PostResponse fromEntity(PostEntity savedPost) {
        MemberResponse authorInfo = MemberResponse.from(savedPost.getAuthor());
        return new PostResponse(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(), authorInfo);
    }
}
