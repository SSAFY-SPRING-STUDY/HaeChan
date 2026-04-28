package ssafy.study.ssafystudy.domain.post.controller.dto;

import ssafy.study.ssafystudy.domain.member.entity.MemberEntity;
import ssafy.study.ssafystudy.domain.post.entity.PostEntity;

public record PostRequest(
        String title,
        String content) {

    public PostEntity toEntity(MemberEntity author) {
        return PostEntity.create(title, content, author);
    }
}
