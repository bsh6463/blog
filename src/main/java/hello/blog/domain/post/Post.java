package hello.blog.domain.post;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.member.Member;
import hello.blog.web.dto.PostDto;
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

@Entity @Getter
@EntityListeners(value = AuditingEntityListener.class)
public class Post {

    @Id @GeneratedValue
    @Column(name = "POST_ID")
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

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

    public Post dtoToPost(PostDto postDto){
        this.changeTitle(postDto.getTitle());
        this.changeContent(postDto.getContent());
        return this;
    }

    public PostDto postToDto(Post post){
        return new PostDto(post.getId(),post.getTitle(), post.getContent()
                , post.getMember().memberToDto(), post.getLastModifiedDate());
    }

    public PostDto postToDto(){
        return new PostDto(this.getId(),this.getTitle(), this.getContent()
                , this.getMember().memberToDto(), this.getLastModifiedDate());
    }
}
