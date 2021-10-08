package hello.blog.service.post;

import hello.blog.domain.post.Post;

import java.util.List;

public interface PostService {

    Post savePost(Post post);
    Post findPostById(Long id);
    Post findPostByTitle(String title);
    List<Post> findAll();
    List<Post> findByTitleContains(String title);
    Post updatePost(Long id, Post updatedPost);
    void deletePost(Long id);
    void clearRepository();
}
