package hello.blog.service.post;

import hello.blog.domain.post.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class PostServiceImplTest {

    @Autowired PostServiceImpl postService;

    @Test
    void savePost() {
        //given
        Post post = createPost();

        //when
        Post savedPost = postService.savePost(post);

        //then
        assertThat(savedPost.getId()).isEqualTo(post.getId());
        assertThat(savedPost.getTitle()).isEqualTo("title");
        assertThat(savedPost.getContent()).isEqualTo("content");
    }

    @Test
    void findPostById() {
        //given
        Long id = postService.findPostByTitle("title1").getId();
        //when
        Post findPost = postService.findPostById(id);
        //then
        assertThat(findPost.getTitle()).isEqualTo("title1");
        assertThat(findPost.getContent()).isEqualTo("content1");
    }


    @Test
    void findPostByIdNoResult2(){

        //given
        Long id = 321321L;

        //when
        Post postById = postService.findPostById(id);

        //then
        assertThat(postById.isEmpty()).isTrue();
    }

    @Test
    void findPostByTitle() {
        //given, when
        Post findPost = postService.findPostByTitle("title1");

        //then
        assertThat(findPost.getTitle()).isEqualTo("title1");
        assertThat(findPost.getContent()).isEqualTo("content1");
    }

    @Test
    void findPostByTitleNoResult(){
        Post noResult = postService.findPostByTitle("noResult");
        assertThat(noResult.isEmpty()).isTrue();
    }

    @Test
    void updatePost() {
        //given
        Long id = postService.findPostByTitle("title1").getId();
        Post postParam = new Post("changedTitle", "changedContent");

        //when
        Post updatedPost = postService.updatePost(id, postParam);

        //then
        assertThat(updatedPost.getTitle()).isEqualTo("changedTitle");
        assertThat(updatedPost.getContent()).isEqualTo("changedContent");

    }

    @Test
    void deletePost() {
        //given
        Post post = postService.findPostByTitle("title1");

        //when
        postService.deletePost(post.getId());

        //then
        assertThat(postService.findAll().size()).isEqualTo(6);
    }

    @Test
    void findByTitleContainsTest(){
        //given
        String title = "title1";

        //when
        List<Post> posts = postService.findByTitleContains("title");
        for (Post post : posts) {
            System.out.println("===============================");
            System.out.println("post = " + post.getTitle());
            System.out.println("===============================");
        }

        //when, then
        assertThat(posts.size()).isEqualTo(7);
    }

    @Test
    void findByTitleContainsTestNoResult() {
        //given
        String title = "NoResultPLZ";

        //when
        List<Post> results = postService.findByTitleContains(title);

        //then
        assertThat(results.isEmpty()).isTrue();

    }
    
   /* @Test
    void findAllPageTest(){
        Page page = postService.findAllPaging(0, 2);
        List<List<Post>> pages = page.getPages();

        for (List<Post> posts : pages) {
            System.out.println("===================");
            for (Post post : posts) {
                System.out.println("post title : " + post.getTitle());
            }
            System.out.println("===================");
        }
        assertThat(page.getPages().size()).isEqualTo(4);
        assertThat(page.getPages().get(0).get(0).getTitle()).isEqualTo("title1");

    }*/

    @BeforeEach
    public void beforeEach(){
        postService.savePost(new Post("title1", "content1"));
        postService.savePost(new Post("title2", "content2"));
        postService.savePost(new Post("title3", "content3"));
        postService.savePost(new Post("title4", "content4"));
        postService.savePost(new Post("title5", "content5"));
        postService.savePost(new Post("title6", "content6"));
        postService.savePost(new Post("title7", "content7"));
    }

    @AfterEach
    public void clear(){
        postService.clear();
    }

    public Post createPost(){
        return new Post("title", "content");
    }
}
