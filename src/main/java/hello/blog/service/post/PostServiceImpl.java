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
        return postRepository.findById(id).orElseThrow(NoResultException::new);
    }

    @Override
    public Post findPostByTitle(String title) {
        return  postRepository.findByTitle(title).orElseThrow(NoResultException::new);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll().orElseThrow(NoResultException::new);
    }

    @Override
    public List<Post> findByTitleContains(String title) {
        return postRepository.findByTitleContains(title).orElseThrow(NoResultException::new);
    }

    @Override
    @Transactional
    public Post updatePost(Long id, Post postParam) {
        Post post = postRepository.findById(id).orElseThrow(NoResultException::new);
        post.changeTitle(postParam.getTitle());
        post.changeContent(postParam.getContent());
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post findPost = postRepository.findById(id).orElseThrow(NoResultException::new);
        postRepository.removePost(findPost);

    }

    @Override
    public void clearRepository() {
        postRepository.clear();
    }
}
