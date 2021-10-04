package hello.blog.domain;

import hello.blog.domain.comment.Comment;
import hello.blog.domain.post.Post;
import hello.blog.web.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
public class Board {

    private MemberDto member;

    private Post post;

    private List<Comment> comments;



}
