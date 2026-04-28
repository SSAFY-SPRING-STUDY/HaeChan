package ssafy.study.ssafystudy.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ssafy.study.ssafystudy.domain.post.controller.dto.PostRequest;
import ssafy.study.ssafystudy.domain.post.controller.dto.PostResponse;
import ssafy.study.ssafystudy.domain.post.entity.PostEntity;
import ssafy.study.ssafystudy.domain.post.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponse create(PostRequest request) {
        PostEntity savedPost = postRepository.save(request.toEntity());
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

    public PostResponse update(PostRequest request, Long id, Long authorId) {
        MemberEntity author = memberRepository.findById(authorId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        PostEntity post = postRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!post.getAuthor().getId().equals(author.getId())) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

    public PostResponse update(PostRequest request, Long id) {
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        entity.update(request.title(), request.content());

        return PostResponse.fromEntity(entity);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
