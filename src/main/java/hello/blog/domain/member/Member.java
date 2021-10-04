package hello.blog.domain.member;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.post.Post;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
public class Member {

    @Id @Column(name = "MEMBER_ID")
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    private String name;
    private String userId; //회원이 로그인 시 사용하는 id
    private String password;
    private String email;



    public Member(String name, String userId, String password, String email) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public Member() {
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


}

