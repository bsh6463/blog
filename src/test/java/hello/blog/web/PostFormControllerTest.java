package hello.blog.web;

import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import hello.blog.utils.Authority;
import hello.blog.web.dto.MemberDto;
import hello.blog.web.dto.PostDto;
import hello.blog.web.login.LoginController;
import hello.blog.web.session.SessionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;

import java.util.List;

import static hello.blog.web.session.SessionManager.SESSION_COOKIE_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PostFormControllerTest {

    @Autowired PostFormController postFormController;
    @Autowired PostService postService;
    @Autowired MemberService memberService;
    @Autowired SessionManager sessionManager;
    @Autowired MockMvc mockMvc;
    @Autowired EntityManager em;

    @Test
    void 비로그인_글목록() throws Exception {

        ModelAndView mav = mockMvc.perform(
                get("/posts"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("post/posts"))
                .andExpect(model().attributeExists("posts"))
                .andReturn().getModelAndView();

        List<PostDto> posts = (List<PostDto>) mav.getModel().get("posts");
        assertThat(posts.size()).isEqualTo(2);
    }

    @Test
    void 비로그인_게시글_상세() throws Exception {

        Post post1 = postService.findPostByTitle("test1");
        System.out.println("test1 = " + post1.getId());

        ModelAndView mav = mockMvc.perform(
                get("/posts/{id}", post1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"))
                .andReturn().getModelAndView();

        PostDto post = (PostDto) mav.getModel().get("post");
        assertThat(post.getId()).isEqualTo(post1.getId());
    }

    @Test
    void 비로그인_글작성_시도_GET() throws Exception {

        mockMvc.perform(get("/posts/new/form"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 비로그인_글작성_시도_POST() throws Exception {

        mockMvc.perform(post("/posts/new/form"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void 비로그인_글삭제_시도() throws Exception {

        Post post1 = postService.findPostByTitle("test1");

        String redirectUrl = "/login?redirectURL=/posts/delete/"+post1.getId();
        mockMvc.perform(post("/posts/delete/{id}", post1.getId()))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void 비로그인_글수정_시도_GET() throws Exception {
        Post post1 = postService.findPostByTitle("test1");

        mockMvc.perform(get("/posts/edit/{id}", post1.getId()))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void 비로그인_글수정_시도_POST() throws Exception {
        Post post1 = postService.findPostByTitle("test1");

        mockMvc.perform(post("/posts/edit/{id}", post1.getId()))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void 로그인_글목록() throws Exception {

        //when
        Member member = memberService.findMemberByUserId("testId");

       //then, member가 login
        MemberDto loginMember = member.memberToDto();
        loginMember.setAuthority(Authority.normal);
        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionManager.createSession(loginMember, response);

        //then
        ModelAndView mav = mockMvc.perform(
                get("/posts").requestAttr("member", loginMember).cookie(response.getCookie(SESSION_COOKIE_NAME)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("post/posts"))
                    .andExpect(model().attributeExists("posts"))
                    .andReturn().getModelAndView();

        /**
         * 로그인 정보 비교
         */
        CheckLoginMember(member, mav);

    }

    @Test
    void 로그인_게시글_상세() throws Exception {

        //when
        Member member = memberService.findMemberByUserId("testId");
        Post post1 = postService.findPostByTitle("test1");

        //then, member가 login
        MemberDto loginMember = member.memberToDto();
        loginMember.setAuthority(Authority.normal);
        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionManager.createSession(loginMember, response);

        //post 조회
        ModelAndView mav = mockMvc.perform(
                get("/posts/{id}", post1.getId())
                        .requestAttr("member", loginMember)
                        .cookie(response.getCookie(SESSION_COOKIE_NAME)))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"))
                .andReturn().getModelAndView();

        /**
         * 조회한 게시글 정보 확인
         */
        PostDto post = (PostDto) mav.getModel().get("post");
        assertThat(post.getId()).isEqualTo(post1.getId());
        assertThat(post.getContent()).isEqualTo(post1.getContent());
        assertThat(post.getLastModifiedDate()).isEqualTo(post1.getLastModifiedDate());

        /**
         * 로그인 정보 비교
         */
        CheckLoginMember(member, mav);

    }


    @Test
    void 음수_페이지_번호() throws Exception {
        mockMvc.perform(
                get("/posts?page=-1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 양수_범위_벗어난_페이지_번호() throws Exception {
        /**
         * 테스트 데이터 2, 한 페이지 10개 출력.
         * 페이지 수는 1
         */
        mockMvc.perform(
                get("/posts?page=10"))
                .andExpect(status().is4xxClientError());
    }


    /**
     * 로그인한 유저가 posts페이지 접속했을 때 로그인 정보 확인
     */
    private void CheckLoginMember(Member member, ModelAndView mav) {

        MemberDto returnMember = (MemberDto) mav.getModel().get("member");

        assertThat(returnMember.getId()).isEqualTo(member.getId());
        assertThat(returnMember.getUserId()).isEqualTo(member.getUserId());
        assertThat(returnMember.getName()).isEqualTo(member.getName());
        assertThat(returnMember.getEmail()).isEqualTo(member.getEmail());
    }

    @BeforeEach
    void initTest(){
        Post post1 = new Post("test1", "test1");
        postService.savePost(post1);
        Post post2 = new Post("test2", "test2");
        postService.savePost(post2);

        Member member = new Member("testMember", "testId", "testPW", "test@emaail.com");
        memberService.joinMember(member);

        post1.setMember(member);
        post2.setMember(member);

        em.flush();
        em.clear();
    }

    @AfterEach
    void afterTest(){
        postService.clear();
    }

}