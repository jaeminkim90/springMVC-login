package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // 스프링 빈으로 등록
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @param loginId 입력받은 사용자 아이디
     * @param password 입력받은 사용자 패스워드
     * @return null이면 로그인 실패
     */
    public Member login(String loginId, String password) {
        /* 주석 처리된 코드는 stream 방식 더 간편하게 사용할 수 있다
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId); // id로 멤버를 찾는다
        Member member = findMemberOptional.get(); // Optional에서 member를 꺼낸다
        // id로 찾은 member의 패스워드와 입력받은 패스워드가 같으면 member 객체를 반환한다
        if (member.getPassword().equals(password)) {
            return member;
        } else {
            return null; // 패스워드 일치하지 않을 경우 null을 반환한다
        }
        */

        // Optional 안에 있는 Member를 순환하며 조건이 일치하는 경우 반환하고, 그렇지 않을 경우 null을 반환한다.
        return memberRepository.findByLoginId(loginId).
                filter(memberInOptional -> memberInOptional.getPassword().equals(password))
                .orElse(null);
    }
}
