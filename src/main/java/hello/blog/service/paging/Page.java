package hello.blog.service.paging;

import hello.blog.domain.post.Post;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Page {

    private List<List<Post>> pages = new ArrayList<>();

}
