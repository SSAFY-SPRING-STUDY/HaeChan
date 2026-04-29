package ssafy.study.ssafystudy.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.study.ssafystudy.domain.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByUsername(String username);
}
