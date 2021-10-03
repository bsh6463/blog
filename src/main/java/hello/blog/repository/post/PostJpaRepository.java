package hello.blog.repository.post;

import hello.blog.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostJpaRepository implements PostRepository {

    @PersistenceContext
    private final EntityManager em;

    public Post save(Post post){
        em.persist(post);
        return post;
    }

    public Optional<Post> findById(Long id){
        return Optional.ofNullable(em.find(Post.class, id));
    }

    public Optional<Post> findByTitle(String title){
        return  em.createQuery("select p from Post p where p.title= :title", Post.class)
                .setParameter("title", title)
                .getResultList()
                .stream().findAny();
    }

    public Optional<List<Post>> findAll(){
        return Optional.ofNullable(
                em.createQuery("select p from Post p", Post.class)
                .getResultList());
    }

    public void removePost(Post post){
        em.remove(post);
    }

    public void clear(){
        em.clear();
    }

}
