package hello.blog.domain;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.service.comment.CommentService;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@Rollback(value = false)
public class DomainTest {

    @Autowired MemberService memberService;
    @Autowired PostService postService;
    @Autowired CommentService commentService;
    @Autowired EntityManager em;

    @Test
    public void domainMappingTest(){
        //given
        Member member = createMember();
        Post post = createPost();
        Comment parent = createComment();
        Comment child = new Comment("child");

        //when
        post.setMember(member);
        parent.setPost(post);
        parent.setMember(member);
        child.setParent(parent);

        memberService.joinMember(member);
        postService.savePost(post);
        commentService.saveComment(parent);
        commentService.saveComment(child);

        em.flush();
        em.clear();

        //then
        assertThat(parent.getMember().getName()).isEqualTo(member.getName());
        assertThat(parent.getPost().getTitle()).isEqualTo(post.getTitle());
        assertThat(child.getParent().getId()).isEqualTo(parent.getId());
        assertThat(post.getMember().getName()).isEqualTo(member.getName());


    }

    @Test
    public void commentMappingTest(){
        //given
        Comment parent = createComment();
        Comment child = new Comment("child");

        //when
        child.setParent(parent);
        commentService.saveComment(parent);
        commentService.saveComment(child);

        em.flush();
        em.clear();

        //then
        assertThat(child.getParent().getId()).isEqualTo(parent.getId());
    }

    public Member createMember(){
        return new Member("memberA", "abc123", "123465!", "123@navber.com");
    }

    public Post createPost(){
        return new Post("title", "content123");
    }

    public Comment createComment(){
        return new Comment("parent");
    }

}
