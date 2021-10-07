package hello.blog.web.dto;


import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter

public class CommentDto {

    private Long id;
    private String content;
    private MemberDto member;
    private Long postId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    public void setContent(String content) {
        this.content = content;
    }

    public CommentDto() {
    }

    public CommentDto(Long id, String content, MemberDto member, Long postId, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.postId = postId;
        this.lastModifiedDate = lastModifiedDate;
    }


    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }



}
