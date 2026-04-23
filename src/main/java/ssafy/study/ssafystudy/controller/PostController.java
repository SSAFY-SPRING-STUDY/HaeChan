package ssafy.study.ssafystudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssafy.study.ssafystudy.controller.dto.PostRequest;
import ssafy.study.ssafystudy.controller.dto.PostResponse;
import ssafy.study.ssafystudy.service.PostService;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(@RequestBody PostRequest request) {
        return postService.save(request);
    }


}
