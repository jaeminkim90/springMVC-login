package hello.login.web.session;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 세션 관리
 */
@Component
public class SessionManager {

    private static final String SESSION_COOKIE_NAME = "mySessionId";
    // Session을 저장하는 Map
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성: Object를 넣으면 sessionId를 만든다.
     * 1. sessionId 생성(임의의 추정 불가능한 랜덤 값)
     * 2. 세션 저장소에 sessionId와 보관할 값 저장
     * 3. sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response) {

        // 1~2. 세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        // 3. 쿠키를 생성하고 클라이언트에 전달한다
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        // request 내용에서 인증용 쿠키인 "mySessuinId"가 존재하는지 확인한다.
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        // 없을 경우, null을 반환한다
        if (sessionCookie == null) {
            return null;
        }
        // 있을 경우, 인증용 쿠키인 "mySessuinId"의 value인 UUID를 sessionStore의 키 값으로 진짜 쿠키를 찾는다.
        return sessionStore.get(sessionCookie.getValue());
    }

    /**

     **/
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        // 인증용 쿠키인 "mySessuinId"가 존재하면
        if (sessionCookie != null) {
            // 세션 저장소에서 "mySessuinId"를 키 값으로 가지고 있는 쿠키를 삭제한다.
            sessionStore.remove(sessionCookie.getValue());
        }

    }


    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }

        // 배열을 stream으로 바꿔준다. stream은 배열 객체를 순차적으로 조회한다.
        return Arrays.stream(request.getCookies())
                // filter 조건이 일치하는 배열 객체를 찾는다
                .filter(cookie -> cookie.getName().equals(cookieName))
                // 조건이 맞는 객체가 있으면 반환한다
                .findAny()
                // 없을 경우 null을 반환한다
                .orElse(null);
    }
}
