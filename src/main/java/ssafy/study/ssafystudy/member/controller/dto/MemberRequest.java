package ssafy.study.ssafystudy.member.controller.dto;

import ssafy.study.ssafystudy.member.entity.MemberEntity;

public record MemberRequest(
        String username,
        String password,
        String nickname
) {
    public MemberEntity toEntity() {
        return MemberEntity.create(username, password, nickname);
    }
}
