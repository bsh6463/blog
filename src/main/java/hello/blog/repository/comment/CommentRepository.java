package hello.blog.repository.comment;


import hello.blog.domain.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findAll();
    void removeComment(Comment comment);
    void clear();
}
