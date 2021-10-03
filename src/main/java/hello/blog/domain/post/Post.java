package hello.blog.domain.post;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity @Getter
public class Post {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    private Long viewCnt;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post() {
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
