package hello.blog.web;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.service.member.MemberService;
import hello.blog.web.dto.MemberDto;
import hello.blog.web.dto.MemberForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String members(Model model){
        List<Member> members = memberService.findAll();
        List<MemberDto> memberDtos = members.stream()
                .map(member -> new MemberDto(member.getId(), member.getUserId(), member.getName()
                        , member.getEmail(), member.getLastModifiedDate())).collect(Collectors.toList());
        model.addAttribute("members", memberDtos);
        return "member/members";
    }

    @GetMapping("/{id}")
    public String member(@PathVariable("id") Long id, Model model){
        Member memberById = memberService.findMemberById(id);
        MemberDto memberDto = memberById.memberToDto();
        model.addAttribute("member", memberDto);
        return "member/member";

    }

    @GetMapping("/new/form")
    public String addForm(@ModelAttribute("member") MemberForm memberForm){
        return "member/addMemberForm";
    }

    @PostMapping("/new/form")
    public String save(@ModelAttribute("member") MemberForm memberForm){
        Member member = memberService.joinMember(formToMember(memberForm));
        log.info("new member.name = {}", member.getName());
        log.info("new member.userId = {}", member.getUserId());

        return "redirect:/";
    }


    @PostMapping("/{memberId}/delete")
    public String deleteMember(@PathVariable("memberId") Long id, Model model){
        Member member = memberService.findMemberById(id);
        Member withdrawnMember = memberService.findMemberByUserId("withdrawnMember");

        SetMemberAsWithdrawnMember(member, withdrawnMember);

        memberService.removeMember(id);
        model.addAttribute("status", "withdrawn");

        return "redirect:/logout";
    }

    private void SetMemberAsWithdrawnMember(Member member, Member withdrawnMember) {
        List<Comment> comments = member.getComments();
        for (Comment comment : comments) {
            comment.setMember(withdrawnMember);
        }

        List<Post> posts = member.getPosts();
        for (Post post : posts) {
            post.setMember(withdrawnMember);
        }
    }

    public Member formToMember(MemberForm memberForm){
        return new Member(memberForm.getName(), memberForm.getUserId(), memberForm.getPassword(),memberForm.getEmail());
    }


}
