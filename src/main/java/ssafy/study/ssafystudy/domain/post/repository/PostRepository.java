package ssafy.study.ssafystudy.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.study.ssafystudy.domain.post.entity.PostEntity;


public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
