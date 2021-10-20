package hello.blog.service.post;

import hello.blog.domain.post.Post;
import hello.blog.service.paging.Page;

import java.util.List;

public interface PostService {

    Post savePost(Post post);
    Post findPostById(Long id);
    Post findPostByTitle(String title);
    List<Post> findAll();
    Page findAllPaging(int offset, int limit);
    List<Post> findByTitleContains(String title);
    Post updatePost(Long id, Post updatedPost);
    void deletePost(Long id);
    void clear();
}
