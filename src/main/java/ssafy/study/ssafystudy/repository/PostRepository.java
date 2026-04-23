package ssafy.study.ssafystudy.repository;

import org.springframework.stereotype.Repository;
import ssafy.study.ssafystudy.entity.PostEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
    private static final List<PostEntity> postDB =  new ArrayList<>();

    public PostEntity save(PostEntity entity) {
        postDB.add(entity);
        return entity;
    }

    public List<PostEntity> findAll() {
        return postDB;
    }

    public Optional<PostEntity> findById(Long id) {
        return postDB.stream()
                .filter(postEntity -> postEntity.getId().equals(id))
                .findFirst();
    }

    public void deleteById(Long id) {
        postDB.removeIf(postEntity -> postEntity.getId().equals(id));
    }
}
