package hello.blog.domain.comment;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    public Comment() {
    }

    public Comment(String content) {
        this.content = content;
    }

    public void changeContent(String content){
        this.content = content;
    }
}
