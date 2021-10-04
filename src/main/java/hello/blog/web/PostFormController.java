package hello.blog.web;

import hello.blog.domain.post.Post;
import hello.blog.repository.post.PostRepository;
import hello.blog.service.post.PostService;
import hello.blog.web.dto.PostDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostFormController {

    private final PostService postService;

    @GetMapping
    public String posts(Model model) {
        List<Post> posts = postService.findAll();
        List<PostDto> postsDto = posts.stream().map(post -> new PostDto(post.getId(),post.getTitle(), post.getContent())).collect(Collectors.toList());

        model.addAttribute("posts", postsDto);

        return "post/posts";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long id, Model model){
        Post post = postService.findPostById(id);
        PostDto postDto = postToDto(post);
        model.addAttribute("post", postDto);
        return "post/post";
    }

    @GetMapping("/new/form")
    public String postForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/addForm";
    }

    @PostMapping("/new/form")
    public String addPost(@ModelAttribute PostDto postDto) {
        Post post = new Post().dtoToPost(postDto);
        postService.savePost(post);
        log.info("new post.title = {}", post.getTitle());
        log.info("new post.content = {}", post.getContent());

        return "redirect:/posts";
    }

    public PostDto postToDto(Post post){
       return  new PostDto(post.getId(), post.getTitle(), post.getContent());
    }


}
