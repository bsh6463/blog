package hello.blog.domain.member;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.post.Post;
import hello.blog.web.dto.MemberDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class Member {

    @Id @Column(name = "MEMBER_ID")
    @GeneratedValue
    private Long id;

    /**
     * em.persist()전에 호출됨.
     */
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @LastModifiedDate
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    //@NotNull
    private String name;

    //@NotNull
    private String userId; //회원이 로그인 시 사용하는 id

    //@NotNull
    private String password;

    //@NotNull
    private String email;



    public Member(String name, String userId, String password, String email) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public Member() {
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void changeName(String newName){
        this.name = newName;
    }

    public void changePassword(String newPassword){
        this.password=newPassword;
    }

    public void changeEmail(String newEmail){
        this.email = newEmail;
    }

    public MemberDto memberToDto(Member member){
        return new MemberDto(member.getId(), member.getUserId(), member.getName(), member.getEmail(), member.getLastModifiedDate());
    }

    public MemberDto memberToDto() {
        return new MemberDto(this.getId(),this.getUserId(), this.getName(), this.getEmail(), this.getLastModifiedDate());
    }

    public Boolean isEmpty(){

        if(this.getId() == null
                && this.getUserId() == null
                && this.getPassword() == null){
            return true;
        }
        return false;
    }


}

