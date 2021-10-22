package hello.blog.web;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.service.comment.CommentService;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import hello.blog.web.dto.CommentDto;
import hello.blog.web.dto.MemberDto;
import hello.blog.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/new")
    public String addComment(@RequestParam("postId") Long postId, @ModelAttribute("commentDto") CommentDto commentDto, BindingResult bindingResult,
                             HttpServletRequest request, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(System.out::println);
        }
        log.info("Comment Controller 시작.");
        log.info("bindingResult: {}", bindingResult.toString());
        //log.info("commentString: {}", commentString);
        log.info("commentDto:{}", commentDto.toString());
        log.info("commentDto content:{}", commentDto.getContent());

        MemberDto memberDto = (MemberDto) sessionManager.getSession(request);
        Member member = memberService.findMemberById(memberDto.getId());
        Post post = postService.findPostById(postId);

        Comment comment = dtoToComment(commentDto);
        //Comment comment= new Comment(commentString);
        comment.setMember(member);
        comment.setPost(post);

        commentService.saveComment(comment);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") Long id, @RequestParam("postId") Long postId, RedirectAttributes redirectAttributes){
        commentService.deleteComment(id);

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }

    public Comment dtoToComment(CommentDto commentDto){
        return new Comment(commentDto.getContent());
    }
}
