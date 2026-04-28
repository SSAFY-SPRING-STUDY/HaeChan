package ssafy.study.ssafystudy.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssafy.study.ssafystudy.domain.auth.component.SessionManager;
import ssafy.study.ssafystudy.domain.auth.util.AuthTokenUtils;
import ssafy.study.ssafystudy.domain.post.controller.dto.PostRequest;
import ssafy.study.ssafystudy.domain.post.controller.dto.PostResponse;
import ssafy.study.ssafystudy.domain.post.service.PostService;
import ssafy.study.ssafystudy.global.exception.CustomException;
import ssafy.study.ssafystudy.global.exception.error.ErrorCode;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final SessionManager sessionManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(@RequestHeader("Authorization") String bearerToken, @RequestBody PostRequest request) {
        if(!AuthTokenUtils.isValidBearerToken(bearerToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String sessionKey = AuthTokenUtils.parseBearerToken(bearerToken);
        Long authorId = sessionManager.getMemberId(sessionKey);

        return postService.create(request, authorId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponse> getPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse updatePost(@PathVariable Long id,
                                   @RequestHeader("Authorization") String bearerToken,
                                   @RequestBody PostRequest request) {
        if(!AuthTokenUtils.isValidBearerToken(bearerToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String sessionKey = AuthTokenUtils.parseBearerToken(bearerToken);
        Long authorId = sessionManager.getMemberId(sessionKey);
        return postService.update(request, id, authorId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id,
                           @RequestHeader("Authorization") String bearerToken
    ) {
        if(!AuthTokenUtils.isValidBearerToken(bearerToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String sessionKey = AuthTokenUtils.parseBearerToken(bearerToken);
        Long authorId = sessionManager.getMemberId(sessionKey);
        postService.delete(id,authorId);
    }

}
