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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private String searchStatus = SearchStatus.statusSearch;
    private String guest = Authority.guest;
    private int offset = 0;
    private int limit = 10;
    private int totalSize;
    private int totalSizeSearch;
    private int numberOfPages;
    private int numberOfSearchPages;

    @PostConstruct
    public int init(){
       totalSize = postService.findAll().size();
        numberOfPages = getNumberOfPages();

        return numberOfPages;
    }

    //@GetMapping
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

    @GetMapping
    public String pages(@RequestParam("page") @Nullable Integer page, Model model, HttpServletRequest request, HttpServletResponse response) {

        MemberDto loginMemberDto = getLoginMember(request);

        if (page == null){
            page = 1;
        }else if (page <= 0 ){
            throw new IllegalArgumentException("page 번호는 양수입니다.");
        }else if (page > numberOfPages){
            throw new IllegalArgumentException("존재하지 않는 페이지 입니다.");
        }

        List<Post> posts = postService.findAllPaging(offset + (page - 1) * limit, limit);
        List<PostDto> postsDto = getPostDtos(posts);

        loginMemberDto = checkNonLoginGuest(loginMemberDto);

        model.addAttribute("member", loginMemberDto);
        model.addAttribute("posts", postsDto);
        model.addAttribute("numberOfPages", numberOfPages);
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("status", SearchStatus.statusNormal);


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
    public String searchPaging(@RequestParam("keyword") String keyword, @RequestParam("page") @Nullable Integer page,
                               Model model, HttpServletRequest request) throws NoResultException {

        if (page == null){
            page = 1;
        }else if (page <= 0 ){
            throw new IllegalArgumentException("page 번호는 음수일 수 없음");
        }else if (page > numberOfPages){
            throw new IllegalArgumentException("존재하지 않는 페이지 입니다.");
        }

        log.info("keyword: {}", keyword);
        MemberDto loginMemberDto = getLoginMember(request);

        //전체 size구하기 위해서 쿼리를 한번 더 날리는게 마음에 안듦..
        totalSizeSearch = postService.findByTitleContains(keyword).size();
        List<Post> posts = postService.findByTitleContainsPaging(keyword,offset + (page - 1) * limit, limit);
        List<PostDto> postsDto = getPostDtos(posts);


        numberOfSearchPages = totalSizeSearch/limit;

        if(numberOfSearchPages == 0){
            numberOfSearchPages=1;
        }else {
            if (totalSizeSearch%limit != 0){
                numberOfSearchPages++;
            }
        }

        log.info("number of pages : {}", numberOfSearchPages);
        loginMemberDto = checkNonLoginGuest(loginMemberDto);
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("posts", postsDto);
        model.addAttribute("searchForm", new SearchForm(keyword));
        model.addAttribute("status", searchStatus);
        model.addAttribute("numberOfPages", numberOfSearchPages);

        return "post/posts";

    }

    //@GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model, HttpServletRequest request) throws NoResultException {

        log.info("keyword: {}", keyword);
        MemberDto loginMemberDto = getLoginMember(request);

        List<Post> posts = postService.findByTitleContains(keyword);
        List<PostDto> postsDto = getPostDtos(posts);

        loginMemberDto = checkNonLoginGuest(loginMemberDto);

        model.addAttribute("member", loginMemberDto);
        model.addAttribute("posts", postsDto);
        model.addAttribute("searchForm", new SearchForm(keyword));
        model.addAttribute("status", searchStatus);

        return "post/posts";

    }

    @GetMapping("/new/form")
    public String postForm(Model model, HttpServletRequest request) throws IllegalAccessException {
        MemberDto loginMemberDto = getLoginMember(request);

        if (hasNoAuthority(loginMemberDto)) {
            throw new IllegalAccessException("비정상적인 접근입니다.");
           // return "redirect:/posts";
        }

        PostDto postDto = new PostDto();
        model.addAttribute("member", loginMemberDto);
        model.addAttribute("post", postDto);

        return "post/addform";
    }

    @PostMapping("/new/form")
    public String addPost(@ModelAttribute("post") PostDto postDto, HttpServletRequest request
                            , Model model) throws IllegalAccessException {
        MemberDto loginMemberDto = getLoginMember(request);

        if (hasNoAuthority(loginMemberDto)) {
            throw new IllegalAccessException("비정상적인 접근입니다.");
            //return "redirect:/posts";
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

        changeNumberOfPages(model);

        return "redirect:/posts";
    }

    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId, HttpServletRequest request
                            , Model model) throws IllegalAccessException {
        MemberDto loginMemberDto = getLoginMember(request);

        PostDto postDto = postService.findPostById(postId).postToDto();

        if (hasNoAuthority(loginMemberDto, postId)) {
            throw new IllegalAccessException("비정상적인 접근입니다.");
            //return "redirect:/posts";
        }

        postService.deletePost(postId);
        changeNumberOfPages(model);

        return "redirect:/posts";
    }

    private void changeNumberOfPages(Model model) {
        numberOfPages = init();
        model.addAttribute("numberOfPages", numberOfPages);
    }

    @GetMapping("/edit/{postId}")
    public String editForm(@PathVariable("postId") Long postId, HttpServletRequest request, Model model) throws IllegalAccessException {
        MemberDto loginMemberDto = getLoginMember(request);

        PostDto postDto = postService.findPostById(postId).postToDto();
        log.info("authority : {}", loginMemberDto.getAuthority());

       if (hasNoAuthority(loginMemberDto, postId)) {
           throw new IllegalAccessException("비정상적인 접근입니다.");
           //return "redirect:/posts";
           }


        getLoginMemberAndAddToModel(request, model);

        model.addAttribute("post", postDto);

        return "post/editform";
    }

    @PostMapping("/edit/{postId}")
    public String editPost(@PathVariable("postId") Long id, @ModelAttribute("post") PostDto postForm
                            ,HttpServletRequest request,Model model, RedirectAttributes redirectAttributes) throws IllegalAccessException {
         log.info("editPost PostMethod 실행");
        MemberDto loginMemberDto = getLoginMember(request);

        if (hasNoAuthority(loginMemberDto, id)) {
            throw new IllegalAccessException("비정상적인 접근입니다.");
            //return "redirect:/posts";
        }

        Post post = editPostUsingDto(id, postForm);

        model.addAttribute("member", loginMemberDto);
        model.addAttribute("post", post.postToDto());
        redirectAttributes.addAttribute("postID", postForm.getId());

        init();
        return "redirect:/posts/{postId}";
    }

    private int getNumberOfPages() {

        int numberOfPages = totalSize/limit;

        if(numberOfPages == 0){
            numberOfPages = 1;
        }else {
            if (totalSize%limit != 0){
                numberOfPages++;
            }
        }
        return numberOfPages;
    }

    private boolean hasNoAuthority(MemberDto loginMemberDto) {
        if (loginMemberDto == null || loginMemberDto.getAuthority().equals(Authority.guest)) {
            return true;
        }

        if(loginMemberDto.getAuthority().equals(Authority.admin)){
            loginMemberDto.setAuthority(Authority.admin);
            log.info("authority,관리자 : {}", loginMemberDto.getAuthority());
            return false;
        }

        return false;
    }

    private boolean hasNoAuthority(MemberDto loginMemberDto, Long postId) {

        log.info("권한 검증");
        Post post = postService.findPostById(postId);

        if (loginMemberDto == null || loginMemberDto.getAuthority().equals(Authority.guest)
        || loginMemberDto.getId() != post.getMember().getId()) {
            //admin 확인
            if (checkAdmin(loginMemberDto)) return false;
            return true;
        }

        //admin 확인
        if (checkAdmin(loginMemberDto)) return false;
        return false;
    }

    private boolean checkAdmin(MemberDto loginMemberDto) {
        if (loginMemberDto.getAuthority().equals(Authority.admin)) {
            log.info("관리자 접속 : {}", loginMemberDto.getAuthority());
            loginMemberDto.setAuthority(Authority.admin);
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

            return loginMemberDto;
        }else if(loginMemberDto.getAuthority().equals(Authority.admin)){
            log.info("관리자 접속");
            loginMemberDto.setAuthority(Authority.admin);
            return loginMemberDto;
        }else {
            log.info("일반 유저 접속");
            loginMemberDto.setAuthority(Authority.normal);
            return loginMemberDto;
        }
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
            if (checkAdmin(loginMemberDto)) return loginMemberDto;
            loginMemberDto.setAuthority(Authority.commentOnly);

            return loginMemberDto;
        }else {
            log.info("일반 유저 접속");
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
