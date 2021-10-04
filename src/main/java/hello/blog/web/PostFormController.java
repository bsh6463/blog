package hello.blog.web;

import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.repository.post.PostRepository;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import hello.blog.web.dto.MemberDto;
import hello.blog.web.dto.PostDto;
import hello.blog.web.session.SessionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostFormController {

    private final PostService postService;
    private final MemberService memberService;
    private final SessionManager sessionManager;

    @GetMapping
    public String posts(Model model, HttpServletRequest request) {
        MemberDto loginMemberDto = (MemberDto) sessionManager.getSession(request);

        List<Post> posts = postService.findAll();
        List<PostDto> postsDto = posts.stream()
                .map(post -> new PostDto(post.getId(),post.getTitle(), post.getContent(),post.getMember().memberToDto())).collect(Collectors.toList());

        model.addAttribute("member", loginMemberDto);
        model.addAttribute("posts", postsDto);

        return "post/posts";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long id, Model model, HttpServletRequest request){
        MemberDto memberDto = (MemberDto) sessionManager.getSession(request);
        Post post = postService.findPostById(id);

        PostDto postDto = postToDto(post);

        model.addAttribute("member", memberDto);
        model.addAttribute("post", postDto);
        return "post/post";
    }

    @GetMapping("/new/form")
    public String postForm(Model model, HttpServletRequest request) {
        MemberDto memberDto = (MemberDto) sessionManager.getSession(request);

        PostDto postDto = new PostDto();
        //postDto.setTitle("TitleTest");
        model.addAttribute("member", memberDto);
        model.addAttribute("post", postDto);

        return "post/addform";
    }

    @PostMapping("/new/form")
    public String addPost(@ModelAttribute("post") PostDto postDto, HttpServletRequest request) {
        MemberDto memberDto = (MemberDto) sessionManager.getSession(request);
        Member findMember = memberService.findMemberById(memberDto.getId());

        log.info("post1.title : {}", postDto.getTitle());
        log.info("post1.content : {}", postDto.getContent());

        Post post = new Post().dtoToPost(postDto);
        post.setMember(findMember);
        postService.savePost(post);
        memberService.joinMember(findMember);

        log.info("new post.title = {}", post.getTitle());
        log.info("new post.content = {}", post.getContent());
        log.info("new post by : {}", memberDto.getUserId());

        return "redirect:/posts";
    }

    public PostDto postToDto(Post post){
        return new PostDto(post.getId(),post.getTitle(), post.getContent());
    }

}
