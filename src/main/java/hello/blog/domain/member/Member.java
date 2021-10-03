package hello.blog.domain.member;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity @Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String memberId; //회원이 로그인 시 사용하는 id

    private String password;

    private String email;


    public Member(String name, String memberId, String password, String email) {
        this.name = name;
        this.memberId = memberId;
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

