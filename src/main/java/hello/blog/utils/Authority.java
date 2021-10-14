package hello.blog.utils;

public interface Authority {

    String admin = "admin"; //모든 권한
    String normal = "normal"; //해당 글 + 본인 댓글에 대한 권한
    String commentOnly = "commentOnly"; //댓글 작성, 본인 댓글만 수정 가능.
    String guest = "guest"; //모든 권한 없음.

}
