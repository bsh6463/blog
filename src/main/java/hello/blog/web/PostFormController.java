package hello.blog.web;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.service.member.MemberService;
import hello.blog.service.post.PostService;
import hello.blog.utils.Authority;
import hello.blog.utils.SearchStatus;
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

import javax.persistence.NoResultException;
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
    private String status = SearchStatus.status;
    private String guest = Authority.guest;


    @GetMapping
    public String posts(Model model, HttpServletRequest request) {

        MemberDto loginMemberDto = getLoginMember(request);

        List<Post> posts = postService.findAll();
        List<PostDto> postsDto = getPostDtos(posts);

        loginMemberDto = checkNonLoginGuest(loginMemberDto);
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("posts", postsDto);
        model.addAttribute("searchForm", new SearchForm());


        return "post/posts";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long postId, Model model, HttpServletRequest request){

        Post post = postService.findPostById(postId);
        PostDto postDto = post.postToDto();

        MemberDto loginMemberDto = getLoginMember(request);

        loginMemberDto = checkAuthorization(postId, loginMemberDto);

        List<Comment> comments = post.getComments();
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> new CommentDto(comment.getId(), comment.getContent(), comment.getMember().memberToDto(),
                        postDto.getId(), comment.getLastModifiedDate())).collect(Collectors.toList());

        model.addAttribute("member", loginMemberDto);
        model.addAttribute("post", postDto);
        model.addAttribute("comments", commentDtos);
        model.addAttribute("commentDto", new CommentDto());

        return "post/post";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model, HttpServletRequest request) throws NoResultException {

        log.info("keyword: {}", keyword);
        MemberDto loginMemberDto = getLoginMember(request);

        List<Post> posts = postService.findByTitleContains(keyword);
        List<PostDto> postsDto = getPostDtos(posts);

        loginMemberDto = checkNonLoginGuest(loginMemberDto);
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("posts", postsDto);
        model.addAttribute("searchForm", new SearchForm(keyword));
        model.addAttribute("status", status);

        return "post/posts";
    }

    @GetMapping("/new/form")
    public String postForm(Model model, HttpServletRequest request) {
        MemberDto loginMemberDto = getLoginMember(request);

        if (hasNoAuthority(loginMemberDto)) return "redirect:/posts";

        PostDto postDto = new PostDto();
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("post", postDto);

        return "post/addform";
    }

    @PostMapping("/new/form")
    public String addPost(@ModelAttribute("post") PostDto postDto, HttpServletRequest request) {
        MemberDto loginMemberDto = getLoginMember(request);

        if (hasNoAuthority(loginMemberDto)) return "redirect:/posts";

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

    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId, HttpServletRequest request){
        MemberDto loginMemberDto = getLoginMember(request);

        PostDto postDto = postService.findPostById(postId).postToDto();

        if (hasNoAuthority(loginMemberDto, postDto)) return "redirect:/posts";


        postService.deletePost(postId);

        return "redirect:/posts";
    }

    @GetMapping("/edit/{postId}")
    public String editForm(@PathVariable("postId") Long postId, HttpServletRequest request, Model model){
        MemberDto loginMemberDto = getLoginMember(request);

        PostDto postDto = postService.findPostById(postId).postToDto();
        log.info("authority : {}", loginMemberDto.getAuthority());

        if (hasNoAuthority(loginMemberDto, postDto)) return "redirect:/posts";

        getLoginMemberAndAddToModel(request, model);

        model.addAttribute("post", postDto);

        return "post/editform";
    }

    @PostMapping("/edit/{postId}")
    public String editPost(@PathVariable("postId") Long id, @ModelAttribute("post") PostDto postDto
                            ,HttpServletRequest request,Model model, RedirectAttributes redirectAttributes){

        MemberDto loginMemberDto = getLoginMember(request);

        if (hasNoAuthority(loginMemberDto, postDto)) return "redirect:/posts";

        Post post = editPostUsingDto(id, postDto);


        model.addAttribute("member", loginMemberDto);
        model.addAttribute("post", post.postToDto());
        redirectAttributes.addAttribute("postID", postDto.getId());
        return "redirect:/posts/{postId}";
    }

    private boolean hasNoAuthority(MemberDto loginMemberDto) {
        if (loginMemberDto == null || loginMemberDto.getAuthority().equals(Authority.guest)) {
            return true;
        }
        return false;
    }

    private boolean hasNoAuthority(MemberDto loginMemberDto, PostDto postDto) {
        if (loginMemberDto == null || loginMemberDto.getAuthority().equals(Authority.guest)
        || loginMemberDto.getId() != postDto.getMember().getId()) {
            return true;
        }
        return false;
    }

    private MemberDto checkNonLoginGuest(MemberDto loginMemberDto) {
        if (loginMemberDto == null){
            log.info("비 로그인 사용자 접속");
            loginMemberDto = new MemberDto();
            loginMemberDto.setUserId(Authority.guest);
            loginMemberDto.setAuthority(Authority.guest);
        }

        return loginMemberDto;

    }

    private MemberDto checkAuthorization(Long postId, MemberDto loginMemberDto) {

        PostDto postDto = postService.findPostById(postId).postToDto();

        if (loginMemberDto == null){
            log.info("비 로그인 사용자 접속");
            loginMemberDto = new MemberDto();
            loginMemberDto.setUserId(guest);
            loginMemberDto.setAuthority(Authority.guest);

            return loginMemberDto;

        }else if(!loginMemberDto.getId().equals(postDto.getMember().getId())){
            if(loginMemberDto.getAuthority().equals(Authority.admin)){
                loginMemberDto.setAuthority(Authority.admin);
                return loginMemberDto;
            }
            loginMemberDto.setAuthority(Authority.commentOnly);

            return loginMemberDto;
        }else {
            loginMemberDto.setAuthority(Authority.normal);

            return loginMemberDto;
        }

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
