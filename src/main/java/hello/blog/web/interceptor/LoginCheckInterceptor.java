package hello.blog.web.interceptor;

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
        log.info("login 인증 체그 interceptor 실행");

        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("mySessionId") == null){
            log.info("비로그인 자용자 접속 시도");

            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }
        return true;
    }
}
