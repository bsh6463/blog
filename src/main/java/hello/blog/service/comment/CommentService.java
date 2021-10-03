package hello.blog.service.comment;

import hello.blog.domain.comment.Comment;


import java.util.List;

public interface CommentService {


    Comment saveComment(Comment comment);
    Comment findCommentById(Long id);
    List<Comment> findAll();
    Comment updateComment(Long id, Comment updatedPost);
    void deleteComment(Long id);
    void clearRepository();

}
