package hello.blog.repository.member;

import hello.blog.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    public Member save(Member member);
    public Optional<Member> findMemberById(Long id);
    public Optional<Member> findMemberByName(String name);
    public List<Member> findAll();
    public void removeMember(Member member);
    public void removeById(Long id);
    public void clear();

}
