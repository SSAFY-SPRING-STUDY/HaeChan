package ssafy.study.ssafystudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ssafy.study.ssafystudy.controller.dto.PostRequest;
import ssafy.study.ssafystudy.controller.dto.PostResponse;
import ssafy.study.ssafystudy.entity.PostEntity;
import ssafy.study.ssafystudy.repository.PostRepository;

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
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return PostResponse.fromEntity(entity);
    }
}
