package hello.blog.service.member;

import hello.blog.domain.member.Member;
import hello.blog.repository.member.MemberJpaRepository;
import hello.blog.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member joinMember(Member member) {
       return memberRepository.save(member);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    public void saveMember(Member member){
        memberRepository.save(member);
    }

    @Override
    public Member findMemberById(Long memberId) {
        Member findMember = memberRepository.findMemberById(memberId).orElseGet(Member::new);
        return findMember;
    }

    @Override
    public Member findMemberByName(String memberName) {
        return memberRepository.findMemberByName(memberName).orElseGet(Member::new);
    }

    @Override
    public Member findMemberByUserId(String userId) {

        return memberRepository.findMemberByUserId(userId).orElseGet(Member::new);
    }

    @Override
    @Transactional
    public void updateMember(Long id, Member memberParam) {
        Member findMember = findMemberById(id);
        updateFindMember(memberParam, findMember);
        saveMember(findMember);
    }

    @Override
    @Transactional
    public void removeMember(Long memberId) {
        memberRepository.removeById(memberId);
    }

    @Override
    public void clearRepository() {
        memberRepository.clear();
    }

    private void updateFindMember(Member memberParam, Member findMember) {
        findMember.changeName(memberParam.getName());
        findMember.changePassword(memberParam.getPassword());
        findMember.changeEmail(memberParam.getEmail());
    }
}
