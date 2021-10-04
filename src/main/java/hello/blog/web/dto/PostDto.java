package hello.blog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostDto {

    private Long id;//post의 id
    private String title;
    private String content;
    private MemberDto member;


    public PostDto() {
    }

    public PostDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public PostDto(Long id, String title, String content, MemberDto memberDto) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = memberDto;
    }

    public void setMember(MemberDto member) {
        this.member = member;
    }
}
