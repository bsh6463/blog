package hello.blog.service.comment;

import hello.blog.domain.comment.Comment;
import hello.blog.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(NoResultException::new);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll().orElseThrow(NoResultException::new);
    }

    @Override
    @Transactional
    public Comment updateComment(Long id, Comment updatedPost) {
        Comment comment = commentRepository.findById(id).orElseThrow(NoResultException::new);
        comment.changeContent(updatedPost.getContent());
        return  commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.removeComment(findCommentById(id));

    }

    @Override
    public void clearRepository() {
        commentRepository.clear();
    }
}
