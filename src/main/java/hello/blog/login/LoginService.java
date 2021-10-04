package hello.blog.login;

import hello.blog.domain.member.Member;
import hello.blog.service.member.MemberService;
import hello.blog.web.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberService memberService;

    public MemberDto login(String userId, String password){

        Member findMember = memberService.findMemberByUserId(userId);

        if(!findMember.getPassword().equals(password)){
            return null;
        }

        return findMember.memberToDto(findMember);

    }
}
