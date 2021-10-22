package hello.blog.exceptionResolver;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class MyExceptionResolver implements HandlerExceptionResolver {

    @SneakyThrows
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        if (ex instanceof IllegalArgumentException){
            log.info("Resolve IllegalArgumentException as 400");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
            return new ModelAndView();
        }
        else if (ex instanceof IllegalAccessException){
            log.info("Resolve IllegalAccessException as 400");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 접근입니다.");
            return new ModelAndView();
        }

        return null;
    }
}
