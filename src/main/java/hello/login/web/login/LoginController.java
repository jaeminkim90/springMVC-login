package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService; // 로그인 검증 로직을 가지고 있는 service 객체가 필요하다.

    // 로그인 페이지 요청 시 로그인 폼을 생성하고 화면으로 보낸
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    // Post 요청 시, 검증을 담당하는 @Valid와 에러 메시지를 관리하는 BindingResult가 필요하다
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult) {

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

        // 로그인 성공 처리 TODO
        return "redirect:/";

    }
}

