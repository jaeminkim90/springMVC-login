package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // static 사용
    private static long sequence = 0L; // static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findByuId(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        /*
        List<Member> all = findAll();
        for (Member m : all) {
            if (m.getLoginId().equals(longId)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
        */

        return findAll().stream() // 반복문처럼 전체를 순환함
                .filter(member -> member.getLoginId().equals(loginId)) // 조건에 만족하는 것만 다음 단계로 넘어감
                .findFirst(); // 먼저 나온 것을 반환
    }

    public List<Member> findAll() {
      return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
