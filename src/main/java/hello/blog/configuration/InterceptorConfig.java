package hello.blog.configuration;

import hello.blog.web.interceptor.LoginCheckInterceptor;
import hello.blog.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer{

    private final SessionManager sessionManager;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor(sessionManager))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error",
                        "/", "/members/new/form", "/posts", "/posts/*",
                        "/login", "/logout");
    }


}
