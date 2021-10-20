package hello.blog.service.post;

import hello.blog.domain.member.Member;
import hello.blog.domain.post.Post;
import hello.blog.repository.post.PostRepository;
import hello.blog.service.paging.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.NoResultException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseGet(Post::new);
    }

    @Override
    public Post findPostByTitle(String title) {
        return  postRepository.findByTitle(title).orElseGet(Post::new);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Page findAllPaging(int offset, int limit) {
        int size = findAll().size();
        int cnt = size/limit;

        if(size%limit != 0){
            cnt++;
        }

        Page page = new Page();

        for (int i=0;i<cnt; i++){
            List<Post> results = postRepository.findAllPaging(offset, limit);
            page.getPages().add(results);

            offset = offset+limit;
        }

        return page;
    }


    @Override
    public List<Post> findByTitleContains(String title) {
        return postRepository.findByTitleContains(title);
    }

    @Override
    @Transactional
    public Post updatePost(Long id, Post postParam) {
        Post post = postRepository.findById(id).orElseGet(Post::new);
        post.changeTitle(postParam.getTitle());
        post.changeContent(postParam.getContent());
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post findPost = postRepository.findById(id).orElseGet(Post::new);
        postRepository.removePost(findPost);

    }

    @Override
    public void clear() {
        postRepository.clear();
    }
}
