package ssafy.study.ssafystudy.member.controller.dto;

import ssafy.study.ssafystudy.member.entity.MemberEntity;

public record MemberResponse(
        Long id,
        String username,
        String nickname
) {
    public static MemberResponse from(MemberEntity savedMember) {
        return new MemberResponse(
                savedMember.getId(),
                savedMember.getUsername(),
                savedMember.getNickname()
        );
    }
}
