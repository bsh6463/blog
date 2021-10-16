package hello.blog.domain.comment;

import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.web.dto.CommentDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "COMMENT_ID")
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

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void changeContent(String content){
        this.content = content;
    }

    public Comment dtoToComment(CommentDto commentDto){
        return new Comment(commentDto.getContent());
    }

    public Boolean isEmpty(){

        if(this.getId() == null
            && this.getContent() == null
            && this.getMember() == null
            && this.getMember() == null){
            return true;
        }
        return false;
    }
}
