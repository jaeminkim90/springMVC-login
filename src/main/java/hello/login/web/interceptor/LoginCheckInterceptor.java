package hello.login.web.interceptor;

import hello.login.web.SessionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 = {}", requestURI);

        HttpSession session = request.getSession();

        // 세션이 아예 없어나, 원하는 세션이 아닐 경우
        if (session == null || session.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            // 로그인으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false; // 더 이상 진행하지 않음

        }
        return true;
    }
}
