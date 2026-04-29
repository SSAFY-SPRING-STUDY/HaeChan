package ssafy.study.ssafystudy.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.study.ssafystudy.domain.member.entity.MemberEntity;
import ssafy.study.ssafystudy.domain.member.repository.MemberRepository;
import ssafy.study.ssafystudy.domain.post.controller.dto.PostRequest;
import ssafy.study.ssafystudy.domain.post.controller.dto.PostResponse;
import ssafy.study.ssafystudy.domain.post.entity.PostEntity;
import ssafy.study.ssafystudy.domain.post.repository.PostRepository;
import ssafy.study.ssafystudy.global.exception.CustomException;
import ssafy.study.ssafystudy.global.exception.error.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostResponse create(PostRequest request, Long authorId) {

        MemberEntity author = memberRepository.findById(authorId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        PostEntity savedPost = postRepository.save(request.toEntity(author));
        return PostResponse.fromEntity(savedPost);
    }

    public List<PostResponse> getAllPosts() {
         List<PostEntity> postEntities = postRepository.findAll();
         return postEntities.stream().map(PostResponse::fromEntity).toList();
    }

    public PostResponse getPostById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
        return PostResponse.fromEntity(post);
    }

    @Transactional
    public PostResponse update(PostRequest request, Long id, Long authorId) {
        MemberEntity author = memberRepository.findById(authorId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        PostEntity post = postRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!post.getAuthor().getId().equals(author.getId())) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        post.update(request.title(), request.content());

        return PostResponse.fromEntity(post);
    }

    public void delete(Long id, Long authorId) {
        MemberEntity author = memberRepository.findById(authorId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        PostEntity post = postRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!post.getAuthor().getId().equals(author.getId())) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        postRepository.delete(post);
    }
}
