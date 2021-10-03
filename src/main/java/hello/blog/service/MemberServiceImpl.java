package hello.blog.service;

import hello.blog.domain.member.Member;
import hello.blog.repository.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

    final MemberJpaRepository memberRepository;

    public MemberServiceImpl(MemberJpaRepository memberJpaRepository) {
        this.memberRepository = memberJpaRepository;
    }


    @Override
    @Transactional
    public Member joinMember(Member member) {
       return memberRepository.save(member);
    }

    @Transactional
    public void saveMember(Member member){
        memberRepository.save(member);
    }

    @Override
    public Member findMemberById(Long memberId) {
        Member findMember = memberRepository.findMemberById(memberId).orElse(new Member());
        return findMember;
    }

    @Override
    public Member findMemberByName(String memberName) {
        return memberRepository.findMemberByName(memberName).orElse(new Member());
    }

    @Override
    public void updateMember(Long id, Member memberParam) {
        Member findMember = findMemberById(id);
        updateFindMember(memberParam, findMember);
        saveMember(findMember);
    }

    @Override
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
