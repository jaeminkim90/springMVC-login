package hello.login.web.filter;

import hello.login.web.SessionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request; // 다운 캐스팅
        String requestURI = httpRequest.getRequestURI(); // 요청 URL을 얻는다

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 = {}", requestURI);

            if (isLoginCheckPath(requestURI)) {
                // whitelist가 아닐 경우, 인증 로직이 적용된다.
                log.info("인증 체크 로직 실행 = {} ", requestURI);

                HttpSession session = httpRequest.getSession(false);// 세션이 있는지 찾는다

                // 세션 자체가 없거나, 찾고 있는 세션 값이 없을 경우 미인증 사용자 요청 처리
                if (session == null || session.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 = {} ", requestURI);

                    // 로그인 후에 자동으로 마지막 경로를 찾아갈 수 있도록 요청URL 파라미터를 추가
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }
            // 화이트 리스트일 경우 조건문을 pass하고, 바로 doFilter를 실행한다
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e; // 예외 logging 가능 하지만, 톰캣까지 예외를 보내주어야 함
        } finally {
            log.info("인증 체크 필터 종료 = {} ", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 X(return false일 경우 인증 체크 pass)
     */
    private boolean isLoginCheckPath(String requestURI) {
        // whitelist와 requestURI가 단순하게 매칭되는지 여부를 확인한다
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
