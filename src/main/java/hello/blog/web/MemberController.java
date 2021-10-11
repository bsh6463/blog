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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    Map<String, String> errors = new HashMap<>();

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
    public String save(@ModelAttribute("member") MemberForm memberForm, Model model){
        errors.clear();
        log.info("member save 로직 시작");
        log.info("userId : {}", memberForm.getUserId());

        //검증 로직
        fieldValidation(memberForm, errors, model);

        if(!errors.isEmpty()) {

            log.info("예외발생");
            log.info("errors : {}", errors);
            model.addAttribute("errors", errors);
            return "member/addMemberForm";
        }


        memberJoinAndLogging(memberForm);
        return "redirect:/";
    }


    private Map<String, String> fieldValidation(MemberForm memberForm, Map<String, String> errors, Model model) {
        //userId가 null인 경우
        log.info("검증로직 실행");
        if(!StringUtils.hasText(memberForm.getUserId()) || memberForm.getUserId().equals("")){
            //bindingResult.addError(new FieldError("member", "userId", "ID는 필수 입니다."));
            errors.put("userId", "ID는 필수 입니다.");
        }

        if(memberService.findMemberByUserId(memberForm.getUserId()) != null){
            String message = "사용하신 "+ memberForm.getUserId() + " 는(은) 이미 사용된 ID 입니다.";
            log.info("userId : {}", memberForm.getUserId());
            errors.put("userId", message);
        }

        //PW가 null인경우
        if(!StringUtils.hasText(memberForm.getPassword())){
            //bindingResult.addError(new FieldError("member", "password", "PW는 필수 입니다."));
            errors.put("password", "PW는 필수 입니다.");
        }


        //name이 null인 경우
        if(!StringUtils.hasText(memberForm.getName())){
            //bindingResult.addError(new FieldError("member", "password", "PW는 필수 입니다."));
            errors.put("name", "이름은 필수 입니다.");
        }

        //email이 null인 경우
        if(!StringUtils.hasText(memberForm.getEmail())){
            //bindingResult.addError(new FieldError("member", "password", "PW는 필수 입니다."));
            errors.put("email", "email은 필수 입니다.");
        }

        return errors;
    }

    private void memberJoinAndLogging(@ModelAttribute("member") MemberForm memberForm) {
        Member member = memberService.joinMember(formToMember(memberForm));
        log.info("new member.name = {}", member.getName());
        log.info("new member.userId = {}", member.getUserId());
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
