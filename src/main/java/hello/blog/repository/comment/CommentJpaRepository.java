package hello.blog.repository.comment;

import hello.blog.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentJpaRepository implements CommentRepository{

    @PersistenceContext
    private final EntityManager em;


    @Override
    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }


    @Override
    public Optional<List<Comment>> findAll() {
        return Optional.ofNullable(
                em.createQuery("select c from Comment c", Comment.class).getResultList());
    }


    @Override
    public void removeComment(Comment comment) {
        em.remove(findById(comment.getId()));

    }

    @Override
    public void clear() {
        em.clear();
    }
}
