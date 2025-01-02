package com.example.mytv.application.port.out;

import com.example.mytv.domain.comment.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentPort {
    Comment saveComment(Comment comment);

    void deleteComment(String commentId);

    Optional<Comment> loadComment(String commentId);

    List<Comment> listComment(String videoId, String order, String offset, Integer maxSize);

    List<Comment> listReply(String parentId, String offset, Integer maxSize);

    Optional<Comment> getPinnedComment(String videoId);
}
