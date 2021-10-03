package hello.blog.service;

import hello.blog.domain.member.Member;

import java.util.List;
import java.util.Optional;

/**
 * 멤버 생성, 조회, 변경, 삭제.
 */
public interface MemberService {

    Member joinMember(Member member);

    Member findMemberById(Long memberId);

    Member findMemberByName(String memberName);

    void updateMember(Long id, Member memberParam);

    void removeMember(Long memberId);

    void clearRepository();

}