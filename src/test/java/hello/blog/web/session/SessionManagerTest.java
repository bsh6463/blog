package hello.blog.web.session;

import hello.blog.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.*;


class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void testCreateAndGetSession(){

        //응답으로 세션 생성하기
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member  = new Member();
        sessionManager.createSession(member, response);

        //client에서 쿠키 담아서 요청을 보냄.
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //서버에서 request로 넘어온 세션을 조회 및 비교
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);
    }

    @Test
    void testSessionExpire(){

        //응답으로 세션 생성하기
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member  = new Member();
        sessionManager.createSession(member, response);

        //client에서 쿠키 담아서 요청을 보냄.
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 만료
        sessionManager.expire(request);
        Object result = sessionManager.getSession(request);
        assertThat(result).isNull();
    }


}