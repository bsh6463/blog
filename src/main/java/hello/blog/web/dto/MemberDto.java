package hello.blog.web.dto;

import lombok.Getter;

@Getter
public class MemberDto {

    private Long id;
    private String userId;
    private String name;
    private String email;

    public MemberDto(Long id, String userId, String name, String email) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}
