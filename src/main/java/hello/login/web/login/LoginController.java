package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConstant;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService; // 로그인 검증 로직을 가지고 있는 service 객체가 필요하다.
    private final SessionManager sessionManager;

    // 로그인 페이지 요청 시 로그인 폼을 생성하고 화면으로 보낸
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    // Post 요청 시, 검증을 담당하는 @Valid와 에러 메시지를 관리하는 BindingResult가 필요하다
    // @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            // bindingResult에 문제가 있으면 다시 돌려보낸다.
            return "login/loginForm";
        }

        // 성공로직: form에서 id와 password 꺼내서 검증한다
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // id와 pass가 맞지 않을 경우 bindingResult를 사용하여 에러 메시지를 출력한다
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 오류");
            return "login/loginForm";
        }

        // 로그인 성공 처리

        // 세션 관리자를 통해 세션을 생성하고, 회원 데이터를 보관
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    // Post 요청 시, 검증을 담당하는 @Valid와 에러 메시지를 관리하는 BindingResult가 필요하다
    // @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            // bindingResult에 문제가 있으면 다시 돌려보낸다.
            return "login/loginForm";
        }

        // 성공로직: form에서 id와 password 꺼내서 검증한다
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // id와 pass가 맞지 않을 경우 bindingResult를 사용하여 에러 메시지를 출력한다
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 오류");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    // Post 요청 시, 검증을 담당하는 @Valid와 에러 메시지를 관리하는 BindingResult가 필요하다
    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            // bindingResult에 문제가 있으면 다시 돌려보낸다.
            return "login/loginForm";
        }

        // 성공로직: form에서 id와 password 꺼내서 검증한다
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // id와 pass가 맞지 않을 경우 bindingResult를 사용하여 에러 메시지를 출력한다
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 오류");
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // getSession()은 세션이 있으면 있는 세션을 반환하고, 없으면 신규 세션을 생성한다
        HttpSession session = request.getSession();
        // 세션의 로그인 회원 정보를 보관한다
        session.setAttribute(SessionConstant.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    // 로그아웃 처리 - V1
    // @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    // 로그아웃 처리 - V2
    // @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    // 로그아웃 처리 - V3
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        // 새로 생성하지 않는 조건(false)로 세션을 조회한다
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 정보를 삭제한다
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        // 쿠키의 시간을 삭제하는 방식으로 쿠키 정보를 제거한다
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}

