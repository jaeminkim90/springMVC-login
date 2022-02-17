package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);// session을 받고, 없으면 만들지 않는다

        // 세션이 없을 경우
        if (session == null) {
            return "세션이 없습니다.";
        }

        // asIterator: 세션이 있을 경우, 세션의 name 정보를 조회하는 작업을 전체 반복한다.
        // forEachRemaining: 모든 요소가 처리되거나 작업이 예외를 발생시킬 때까지 남아 있는 각 요소에 대해 지정된 작업을 수행
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        log.info("getMaxInactiveInternal={}", session.getMaxInactiveInterval()); // 활성화 기간
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime())); // 사용자가 마지막 접근한 시간
        log.info("isNew={}", session.isNew()); // 새로 생성된 세션 여부

        return "세션 출력";


    }

}
