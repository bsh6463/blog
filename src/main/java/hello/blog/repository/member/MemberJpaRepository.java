package hello.blog.repository.member;

import hello.blog.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * 순수Jpa 사용.
 */
@Repository
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Optional<Member> findMemberById(Long id){
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public Optional<Member> findMemberByName(String name){
        List<Member> results = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return results.stream().findAny();
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * update는 변경감지를 활용함.
     * 영속 상태인 entity는 Transaction안에서 변경이 일어나면 Transaction종료 시
     * 변경 내용 commit됨.
     */

    public void removeMember(Member member){
        em.remove(member);
    }

    public void removeById(Long id){
        em.remove(findMemberById(id));
    }

    public void clear(){
        em.clear();
    }
}
