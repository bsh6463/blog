package hello.blog.web;

import hello.blog.domain.member.Member;
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
                        , member.getEmail())).collect(Collectors.toList());
        model.addAttribute("members", memberDtos);
        return "member/members";
    }

    @GetMapping("/{id}")
    public String member(@PathVariable("id") Long id, Model model){
        Member memberById = memberService.findMemberById(id);
        MemberDto memberDto = memberToDto(memberById);
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
    public Member formToMember(MemberForm memberForm){
        return new Member(memberForm.getName(), memberForm.getUserId(), memberForm.getPassword(),memberForm.getEmail());
    }

    public MemberDto memberToDto(Member member){
        return new MemberDto(member.getId(), member.getUserId(), member.getName(), member.getEmail());
    }
}
