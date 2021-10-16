package hello.blog.repository.post;

import hello.blog.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);
    Optional<Post> findById(Long id);
    Optional<Post> findByTitle(String title);
    List<Post> findAll();
    List<Post> findByTitleContains(String title);
    void removePost(Post post);
    void clear();
}
