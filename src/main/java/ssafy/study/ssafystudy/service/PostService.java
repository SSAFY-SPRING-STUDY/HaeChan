package ssafy.study.ssafystudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssafy.study.ssafystudy.controller.dto.PostRequest;
import ssafy.study.ssafystudy.controller.dto.PostResponse;
import ssafy.study.ssafystudy.entity.PostEntity;
import ssafy.study.ssafystudy.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponse save(PostRequest request) {
        PostEntity savedPost = postRepository.save(request.toEntity());
        return PostResponse.fromEntity(savedPost);
    }
}
