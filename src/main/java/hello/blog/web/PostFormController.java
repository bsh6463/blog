package hello.blog.web;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import hello.blog.web.dto.CommentDto;
import hello.blog.web.dto.MemberDto;
import hello.blog.web.dto.PostDto;
import hello.blog.web.dto.SearchForm;
import hello.blog.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
    private String status = "search";

    @GetMapping
    public String posts(Model model, HttpServletRequest request) {

        MemberDto loginMemberDto = getLoginMember(request);

        List<Post> posts = postService.findAll();
        List<PostDto> postsDto = getPostDtos(posts);

        loginMemberDto = checkAndReturnNonLoginGuest(loginMemberDto);
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("posts", postsDto);
        model.addAttribute("searchForm", new SearchForm());


        return "post/posts";
    }

    private MemberDto checkAndReturnNonLoginGuest(MemberDto loginMemberDto) {
        if (loginMemberDto == null){
            log.info("비 로그인 사용자 접속");
            loginMemberDto = new MemberDto();
            loginMemberDto.setUserId("guest");
        }
        return loginMemberDto;
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long id, Model model, HttpServletRequest request){
        MemberDto loginMemberDto = getLoginMember(request);


        Post post = postService.findPostById(id);
        PostDto postDto = post.postToDto();

        List<Comment> comments = post.getComments();
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> new CommentDto(comment.getId(), comment.getContent(), comment.getMember().memberToDto(),
                        postDto.getId(), comment.getLastModifiedDate())).collect(Collectors.toList());

        loginMemberDto = checkAndReturnNonLoginGuest(loginMemberDto);
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("post", postDto);
        model.addAttribute("comments", commentDtos);
        model.addAttribute("commentDto", new CommentDto());



        return "post/post";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model, HttpServletRequest request){

        log.info("keyword: {}", keyword);
        MemberDto loginMemberDto = getLoginMember(request);

        List<Post> posts = postService.findByTitleContains(keyword);
        List<PostDto> postsDto = getPostDtos(posts);

        loginMemberDto = checkAndReturnNonLoginGuest(loginMemberDto);
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("posts", postsDto);
        model.addAttribute("searchForm", new SearchForm(keyword));
        model.addAttribute("status", status);

        return "post/posts";
    }

    @GetMapping("/new/form")
    public String postForm(Model model, HttpServletRequest request) {
        MemberDto loginMemberDto = getLoginMember(request);

        if (loginMemberDto == null){
            return "redirect:/posts";
        }

        PostDto postDto = new PostDto();
        //postDto.setTitle("TitleTest");
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("post", postDto);

        return "post/addform";
    }

    @PostMapping("/new/form")
    public String addPost(@ModelAttribute("post") PostDto postDto, HttpServletRequest request) {
        MemberDto loginMemberDto = getLoginMember(request);

        if (loginMemberDto == null){
            return "redirect:/posts";
        }

        Member findMember = memberService.findMemberById(loginMemberDto.getId());

        log.info("post1.title : {}", postDto.getTitle());
        log.info("post1.content : {}", postDto.getContent());

        Post post = new Post().dtoToPost(postDto);
        post.setMember(findMember);
        postService.savePost(post);
        memberService.joinMember(findMember);

        log.info("new post.title = {}", post.getTitle());
        log.info("new post.content = {}", post.getContent());
        log.info("new post by : {}", loginMemberDto.getUserId());

        return "redirect:/posts";
    }

    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long id, HttpServletRequest request){
        MemberDto loginMemberDto = getLoginMember(request);
        if (loginMemberDto == null){
            return "redirect:/posts";
        }

        postService.deletePost(id);

        return "redirect:/posts";
    }

    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable("postId") Long id, HttpServletRequest request, Model model){
        MemberDto loginMemberDto = getLoginMember(request);
        if (loginMemberDto == null){
            return "redirect:/posts";
        }

        getLoginMemberAndAddToModel(request, model);

        PostDto postDto = postService.findPostById(id).postToDto();
        model.addAttribute("post", postDto);

        return "post/editform";
    }

    @PostMapping("/{postId}/edit")
    public String editPost(@PathVariable("postId") Long id, @ModelAttribute("post") PostDto postDto
                            ,HttpServletRequest request,Model model, RedirectAttributes redirectAttributes){

        MemberDto loginMemberDto = getLoginMember(request);
        if (loginMemberDto == null){
            return "redirect:/posts";
        }

        Post post = editPostUsingDto(id, postDto);

        model.addAttribute("member", loginMemberDto);
        model.addAttribute("post", post.postToDto());
        redirectAttributes.addAttribute("postID", postDto.getId());
        return "redirect:/posts/{postId}";
    }

    private Post editPostUsingDto(Long id, PostDto postDto) {
        Post post= postService.findPostById(id);
        post.changeTitle(postDto.getTitle());
        post.changeContent(postDto.getContent());
        postService.savePost(post);
        return post;
    }

    private void getLoginMemberAndAddToModel(HttpServletRequest request, Model model) {
        MemberDto loginMember = getLoginMember(request);
        model.addAttribute("member", loginMember);
    }

    private MemberDto getLoginMember(HttpServletRequest request) {
        return (MemberDto) sessionManager.getSession(request);
    }

    private List<PostDto> getPostDtos(List<Post> posts) {
        return posts.stream()
                .map(post -> new PostDto(post.getId(),post.getTitle(), post.getContent(),post.getMember().memberToDto(),post.getLastModifiedDate())).collect(Collectors.toList());
    }


}
