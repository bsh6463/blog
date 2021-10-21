package hello.blog.repository.post;

import hello.blog.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);
    Optional<Post> findById(Long id);
    Optional<Post> findByTitle(String title);
    List<Post> findAll();
    List<Post> findAllPaging(int offset, int limit);
    List<Post> findByTitleContains(String title);
    List<Post> findByTitleContainsPaging(String title, int offset, int limit);
    void removePost(Post post);
    void clear();
}
