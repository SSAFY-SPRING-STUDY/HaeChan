package ssafy.study.ssafystudy.repository;

import org.springframework.stereotype.Repository;
import ssafy.study.ssafystudy.entity.PostEntity;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {
    private static final List<PostEntity> postDB =  new ArrayList<>();

    public PostEntity save(PostEntity entity) {
        postDB.add(entity);
        return entity;
    }
}
