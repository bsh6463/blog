package hello.blog.repository.post;

import hello.blog.domain.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    public List<Post> findAll(){
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

   public List<Post> findAllPaging(int offset, int limit){
        return em.createQuery("select p from Post p order by p.id asc", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Post> findByTitleContains(String keyword) {
        String query = "%"+keyword+"%";
        log.info("query: {}", query);
        return em.createQuery("select p from Post p where p.title like :keyword", Post.class)
                .setParameter("keyword", query)
                .getResultList();
    }


    public void removePost(Post post){
        em.remove(post);
    }

    public void clear(){
        em.clear();
    }

}
