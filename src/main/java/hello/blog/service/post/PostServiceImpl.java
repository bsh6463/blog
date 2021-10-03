package hello.blog.service.post;

import hello.blog.domain.post.Post;
import hello.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.NoResultException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new NoResultException());
    }

    @Override
    public Post findPostByTitle(String title) {
        return  postRepository.findByTitle(title).orElseThrow(() -> new NoResultException());
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll().orElseThrow(() -> new NoResultException());
    }

    @Override
    public Post updatePost(Long id, Post postParam) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NoResultException());
        post.changeTitle(postParam.getTitle());
        post.changeContent(postParam.getContent());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        Post findPost = postRepository.findById(id).orElseThrow(() -> new NoResultException());
        postRepository.removePost(findPost);

    }

    @Override
    public void clearRepository() {
        postRepository.clear();
    }
}
