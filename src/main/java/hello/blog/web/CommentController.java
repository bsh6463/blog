package hello.blog.web;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.service.comment.CommentService;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import hello.blog.web.dto.CommentDto;
import hello.blog.web.dto.MemberDto;
import hello.blog.web.dto.PostDto;
import hello.blog.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final SessionManager sessionManager;
    private final MemberService memberService;
    private final PostService postService;

    @PostMapping("/new/{postId}")
    public String addComment(@PathVariable("postId") Long postId, @ModelAttribute("comment")CommentDto commentDto, HttpServletRequest request, RedirectAttributes redirectAttributes){
        MemberDto memberDto = (MemberDto) sessionManager.getSession(request);
        Member member = memberService.findMemberById(memberDto.getId());
        Post post = postService.findPostById(postId);

        Comment comment = dtoToComment(commentDto);
        comment.setMember(member);
        comment.setPost(post);

        commentService.saveComment(comment);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }

    public Comment dtoToComment(CommentDto commentDto){
        return new Comment(commentDto.getContent());
    }
}
