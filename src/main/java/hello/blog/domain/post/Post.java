package hello.blog.domain.post;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
public class Post {

    @Id @GeneratedValue
    @Column(name = "POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "post")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    private String title;
    private String content;
    private Long viewCnt;


    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post() {
    }

    //연관관계 편의 메서드
    public void setMember(Member member){
        this.member = member;
        member.getPosts().add(this);
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content=content;
    }

    public void addViewCnt(){
        viewCnt++;
    }
}
