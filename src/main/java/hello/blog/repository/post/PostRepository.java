package hello.blog.repository.post;

import hello.blog.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    public Post save(Post post);
    public Optional<Post> findById(Long id);
    public Optional<Post> findByTitle(String title);
    public Optional<List<Post>> findAll();
    public void removePost(Post post);
    public void clear();
}
