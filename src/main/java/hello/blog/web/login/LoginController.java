package hello.blog.web.login;

import hello.blog.login.LoginService;
import hello.blog.utils.Authority;
import hello.blog.web.dto.MemberDto;
import hello.blog.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }


    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm form, Model model,
                        BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        MemberDto loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null){
            bindingResult.reject("loginFail", "id또는 pw정보가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공
        //관리자 확인
        if(form.getLoginId().equals("admin")){
            loginMember.setAuthority(Authority.admin);
        }else {
            loginMember.setAuthority(Authority.normal);
        }
        model.addAttribute("member", loginMember);
        //세션으로 쿠키 처리.
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logoutWithdrawn(HttpServletRequest request, RedirectAttributes redirectAttributes){

        sessionManager.expire(request);
        String status = (String) request.getAttribute("status");
        if(status == "withdrawn"){
            redirectAttributes.addAttribute("message", "withdrawn");
            return "redirect:/";
        }

        return "redirect:/";
    }

}
