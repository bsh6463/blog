package hello.blog.web;

import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.repository.member.MemberRepository;
import hello.blog.repository.post.PostRepository;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import hello.blog.web.session.SessionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostFormController.class)
@AutoConfigureWebMvc
@MockBean(JpaMetamodelMappingContext.class)
class PostFormControllerTest {

    @MockBean PostService postService;
    @MockBean PostRepository postRepository;
    @MockBean MemberService memberService;
    @MockBean MemberRepository memberRepository;
    @MockBean SessionManager sessionManager;
    @Autowired MockMvc mockMvc;


    @Test
    @DisplayName("비로그인, 글목록 접속")
    void findPostsTest() throws Exception {
        mockMvc.perform(
                get("/posts"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("post/posts"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    @DisplayName("비로그인, 글 상세 조회")
    void nonLoginAddPostTest() throws Exception {

        mockMvc.perform(
                get("/posts/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"));
    }

}