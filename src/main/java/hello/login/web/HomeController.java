package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //  @GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/")
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
}
