package ssafy.study.ssafystudy.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssafy.study.ssafystudy.domain.member.controller.dto.MemberRequest;
import ssafy.study.ssafystudy.domain.member.controller.dto.MemberResponse;
import ssafy.study.ssafystudy.domain.member.entity.MemberEntity;
import ssafy.study.ssafystudy.domain.member.repository.MemberRepository;
import ssafy.study.ssafystudy.global.exception.CustomException;
import ssafy.study.ssafystudy.global.exception.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse join(MemberRequest request) {
        MemberEntity member = request.toEntity();
        MemberEntity savedMember = memberRepository.save(member);

        return MemberResponse.from(savedMember);
    }

    public MemberResponse getMemberInfo(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }
}
