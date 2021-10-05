package hello.blog.web;

import hello.blog.service.member.MemberService;
import hello.blog.web.dto.MemberDto;
import hello.blog.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final SessionManager sessionManager;

    @GetMapping("/")
    public String homeLogin(HttpServletRequest request, Model model){

        //세션에 저장된 정보
        MemberDto memberDto = (MemberDto) sessionManager.getSession(request);

        //세션에 회원 정보가 없는 경우?
        if(memberDto == null){
            log.info("session out");
            return "home";
        }

        model.addAttribute("member", memberDto);
        log.info("user id = {}",memberDto.getUserId());
        return "loginHome";

    }
}
