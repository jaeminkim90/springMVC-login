package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;


class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        // 스프링은 모형 response를 제공한다
        MockHttpServletResponse response = new MockHttpServletResponse();

        // 세션 생성: 내부에서 UUID를 만들고 쿠키로 반환한다
        Member member = new Member();
        sessionManager.createSession(member, response);

        // 요청에 응답 쿠키 저장(브라우저 요청에 쿠키가 담겨있는 상황을 가정한다)
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        // 세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        // 세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();


    }
}
