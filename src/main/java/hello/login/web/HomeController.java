package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //  @GetMapping("/")
    public String home() {
        return "home";
    }


    // @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        // 쿠키가 없는 사용자는 home으로 보낸다
        if (memberId == null) {
            return "home";
        }

        // 쿠키를 가지고 있는 사용자의 Member 객체를 조회한다
        Member loginMember = memberRepository.findById(memberId);

        // 객체가 없을 경우 home으로 보낸다
        if (loginMember == null) {
            return "home";
        }

        // 객체가 있을 경우 모델에 해당 객체를 담아 '로그인 사용자 전용 home 화면'으로 보낸다
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // 세션 관리자에 저장된 회원 정보를 조회
        Member member = (Member)sessionManager.getSession(request);

        // 객체가 없을 경우 home으로 보낸다
        if (member == null) {
            return "home";
        }

        // 객체가 있을 경우 모델에 해당 객체를 담아 '로그인 사용자 전용 home 화면'으로 보낸다
        model.addAttribute("member", member);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // 로그인 화면에서 세션이 생성되지 않도록 false 처리
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        // 상수를 이용해 세션에서 로그인 객체를 꺼낸다
        Member loginMember = (Member) session.getAttribute(SessionConstant.LOGIN_MEMBER);

        // 세션에 회원 데이터가 없으면 home으로 이동
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(
            // @SesseionAttribute는 속성에 해당하는 세션을 가지고 있는 Member 객체를 꺼낸다
            @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        // 세션에 회원 데이터가 없으면 home으로 이동
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {

        // 세션에 회원 데이터가 없으면 home으로 이동
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
