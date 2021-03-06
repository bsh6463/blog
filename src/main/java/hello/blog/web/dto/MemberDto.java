package hello.blog.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class MemberDto {

    private Long id;
    private String userId;
    private String name;
    private String email;
    private String authority;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    public MemberDto(Long id, String userId, String name, String email, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.lastModifiedDate = lastModifiedDate;
    }

    public MemberDto() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


}
