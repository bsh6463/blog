package hello.blog.web.interceptor;

import hello.blog.web.dto.MemberDto;
import hello.blog.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("login 인증 체크 interceptor 실행");

        MemberDto loginMember = (MemberDto) sessionManager.getSession(request);

        if (loginMember == null){
            log.info("비로그인 자용자 접속 시도");

            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }else {
            log.info("login 인증 체크 interceptor 성공");
            return true;
        }

    }
}
