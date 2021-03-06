package hello.blog;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.service.comment.CommentService;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

    private final MemberService memberService;
    private final CommentService commentService;
    private final PostService postService;


//    @PostConstruct
//    @Transactional
//    public void init(){
//
//        Member admin = new Member("Admin", "admin", "admin", "admin@naver.com");
//        memberService.joinMember(admin);
//
//        Member member1 = new Member("test", "test", "test", "test@naver.com");
//        memberService.joinMember(member1);
//
//        Post post1 = new Post("testTitle", "testContent");
//        post1.setMember(member1);
//        postService.savePost(post1);
//
//        Comment comment1 = new Comment("testComment");
//        comment1.setPost(post1);
//        comment1.setMember(member1);
//        commentService.saveComment(comment1);
//
//        Member member2 = new Member("testMember", "test1", "test1", "test1@naver.com");
//        memberService.joinMember(member2);
//
//        Post post2 = new Post("testTitle2", "testContent2");
//        post2.setMember(member2);
//        postService.savePost(post2);
//
//        Comment comment2 = new Comment("testComment2");
//        comment2.setPost(post2);
//        comment2.setMember(member2);
//        commentService.saveComment(comment2);
//
//
//        for (int i =3;i <= 30; i++){
//            Post post = new Post("testTitle"+i, "testContent"+i);
//            post.setMember(member1);
//            postService.savePost(post);
//
//            Comment comment = new Comment("testComment"+i);
//            comment.setPost(post);
//            comment.setMember(member1);
//            commentService.saveComment(comment);
//        }
//
//
//        /**
//         * ????????????
//         */
//        Member withdrawnMember = new Member("????????? ??????", "withdrawnMember", "withdrawnMember", null);
//        memberService.joinMember(withdrawnMember);
//    }


}
