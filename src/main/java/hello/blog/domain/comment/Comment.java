package hello.blog.domain.comment;

import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();

    private String content;

    public void setParent(Comment parent){
        this.parent = parent;
        parent.getChild().add(this);
    }


    public Comment(String content) {
        this.content = content;
    }

    public Comment() {
    }

    public void setMember(Member member){
        this.member = member;
        member.getComments().add(this);
    }

    public void setPost(Post post){
        this.post = post;
        post.getComments().add(this);
    }



    public void changeContent(String content){
        this.content = content;
    }
}
