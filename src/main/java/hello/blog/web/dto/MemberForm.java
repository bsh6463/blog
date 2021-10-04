package hello.blog.web.dto;

import lombok.Getter;

@Getter
public class MemberForm {

    private String userId;
    private String name;
    private String password;
    private String email;

    public MemberForm(String userId, String name, String password, String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
