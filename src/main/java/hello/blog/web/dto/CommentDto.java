package hello.blog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter

public class CommentDto {

    private Long id;
    private String content;
    private MemberDto member;
    private Long postId;

    public void setContent(String content) {
        this.content = content;
    }

    public CommentDto() {
    }

    public CommentDto(Long id, String content, MemberDto memberDto, Long postId) {
        this.id = id;
        this.content = content;
        this.member = memberDto;
        this.postId = postId;
    }
}
