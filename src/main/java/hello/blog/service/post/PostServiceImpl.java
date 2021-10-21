package hello.blog.service.post;

import hello.blog.domain.post.Post;
import hello.blog.repository.post.PostRepository;
import hello.blog.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseGet(Post::new);
    }

    @Override
    public Post findPostByTitle(String title) {
        return  postRepository.findByTitle(title).orElseGet(Post::new);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllPaging(int offset, int limit) {
        return postRepository.findAllPaging(offset, limit);
    }


    @Override
    public List<Post> findByTitleContains(String title) {
        return postRepository.findByTitleContains(title);
    }

    @Override
    public List<Post> findByTitleContainsPaging(String title, int offset, int limit) {
        return postRepository.findByTitleContainsPaging(title, offset, limit);
    }

    @Override
    @Transactional
    public Post updatePost(Long id, Post postParam) {
        Post post = postRepository.findById(id).orElseGet(Post::new);
        post.changeTitle(postParam.getTitle());
        post.changeContent(postParam.getContent());
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post findPost = postRepository.findById(id).orElseGet(Post::new);
        postRepository.removePost(findPost);

    }

    @Override
    public void clear() {
        postRepository.clear();
    }

    private List<PostDto> getPostDtos(List<Post> posts) {
        return posts.stream()
                .map(post -> new PostDto(post.getId(),post.getTitle(), post.getContent(),post.getMember().memberToDto(),post.getLastModifiedDate())).collect(Collectors.toList());
    }


}
