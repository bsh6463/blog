package hello.blog.configuration;

import hello.blog.exceptionResolver.MyExceptionResolver;
import hello.blog.web.interceptor.LoginCheckInterceptor;
import hello.blog.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ExceptionResolverConfig implements WebMvcConfigurer{

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

        resolvers.add(new MyExceptionResolver());
    }
}
