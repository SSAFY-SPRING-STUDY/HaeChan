package ssafy.study.ssafystudy.member.repository;

import org.springframework.stereotype.Repository;
import ssafy.study.ssafystudy.member.entity.MemberEntity;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemberRepository {
    private static final ConcurrentHashMap<Long, MemberEntity> memberDB = new ConcurrentHashMap<>();


    public MemberEntity save(MemberEntity member) {
        memberDB.put(member.getId(), member);
        return memberDB.get(member.getId());
    }

    public Optional<MemberEntity> findByUsername(String username) {
        return memberDB.values()
                .stream()
                .filter(member ->
                        member.getUsername().equals(username))
                .findFirst();
    }
}
