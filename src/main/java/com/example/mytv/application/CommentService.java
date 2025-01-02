package com.example.mytv.application;

import com.example.mytv.adapter.in.api.dto.CommentRequest;
import com.example.mytv.application.port.in.CommentUseCase;
import com.example.mytv.application.port.out.CommentBlockPort;
import com.example.mytv.application.port.out.CommentLikePort;
import com.example.mytv.application.port.out.CommentPort;
import com.example.mytv.application.port.out.LoadUserPort;
import com.example.mytv.domain.comment.Comment;
import com.example.mytv.domain.comment.CommentResponse;
import com.example.mytv.domain.user.User;
import com.example.mytv.exception.BadRequestException;
import com.example.mytv.exception.DomainNotFoundException;
import com.example.mytv.exception.ForbiddenRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService implements CommentUseCase {
    private final CommentPort commentPort;
    private final LoadUserPort loadUserPort;
    private final CommentLikePort commentLikePort;
    private final CommentBlockPort commentBlockPort;

    public CommentService(CommentPort commentPort, @Qualifier("userCachePersistenceAdapter") LoadUserPort loadUserPort, CommentLikePort commentLikePort, CommentBlockPort commentBlockPort) {
        this.commentPort = commentPort;
        this.loadUserPort = loadUserPort;
        this.commentLikePort = commentLikePort;
        this.commentBlockPort = commentBlockPort;
    }

    @Override
    public Comment createComment(User user, CommentRequest commentRequest) {
        var comment = Comment.builder()
            .id(UUID.randomUUID().toString())
            .channelId(commentRequest.getChannelId())
            .videoId(commentRequest.getVideoId())
            .parentId(commentRequest.getParentId())
            .text(commentRequest.getText())
            .authorId(user.getId())
            .publishedAt(LocalDateTime.now())
            .build();

        return commentPort.saveComment(comment);
    }

    @Override
    public Comment updateComment(String commentId, User user, CommentRequest commentRequest) {
        var comment = commentPort.loadComment(commentId)
            .orElseThrow(() -> new DomainNotFoundException("Comment Not Found."));

        if (!Objects.equals(comment.getAuthorId(), user.getId())) {
            throw new ForbiddenRequestException("The request might not be properly authorized.");
        }
        if (!equalMetaData(comment, commentRequest)) {
            throw new BadRequestException("Request metadata is invalid.");
        }

        comment.updateText(commentRequest.getText());

        return commentPort.saveComment(comment);
    }

    @Override
    public void deleteComment(String commentId, User user) {
        var comment = commentPort.loadComment(commentId)
            .orElseThrow(() -> new DomainNotFoundException("Comment Not Found."));

        if (!Objects.equals(comment.getAuthorId(), user.getId())) {
            throw new ForbiddenRequestException("The request might not be properly authorized.");
        }

        commentPort.deleteComment(commentId);
    }

    /**
     * user -> redis user:{userId} 로 조회
     * commentLike -> redis comment:like:{commentId} 로 부터 조회
     */
    @Override
    public CommentResponse getComment(String commentId) {
        // mongodb
        var comment = commentPort.loadComment(commentId)
            .orElseThrow(() -> new DomainNotFoundException("Comment Not Found."));

        return buildComment(comment);
//        // redis
//        var user = loadUserPort.loadUser(comment.getAuthorId())
//            .orElse(User.defaultUser(comment.getAuthorId()));
//        // redis
//        var commentLikeCount = commentLikePort.getCommentLikeCount(comment.getId());
//
//        return CommentResponse.from(comment, user, commentLikeCount);
    }

    @Override
    public List<CommentResponse> listComments(String videoId, String order, String offset, Integer maxSize) {
        var list = commentPort.listComment(videoId, order, offset, maxSize).stream()
            .map(comment -> {
                var user = loadUserPort.loadUser(comment.getAuthorId())
                    .orElse(User.defaultUser(comment.getAuthorId()));
                var commentLikeCount = commentLikePort.getCommentLikeCount(comment.getId());
                var replies = commentPort.listReply(comment.getId(), offset, 100).stream()
                    .map(this::buildComment)
                    .toList();
                return CommentResponse.from(comment, user, commentLikeCount, replies);
            })
            .collect(Collectors.toList());
        commentPort.getPinnedComment(videoId)
            .ifPresent(pinnedComment -> {
                var pinnedCommentResponse = buildComment(pinnedComment);
                list.add(0, pinnedCommentResponse);
            });

        return list;
    }

    @Override
    public List<CommentResponse> listComments(User user, String videoId, String order, String offset, Integer maxSize) {
        var commentBlocks = commentBlockPort.getUserCommentBlocks(user.getId());

        return commentPort.listComment(videoId, order, offset, maxSize).stream()
            .filter(comment -> !commentBlocks.contains(comment.getId()))
            .map(comment -> {
                var author = loadUserPort.loadUser(comment.getAuthorId())
                    .orElse(User.defaultUser(comment.getAuthorId()));
                var commentLikeCount = commentLikePort.getCommentLikeCount(comment.getId());
                var replies = commentPort.listReply(comment.getId(), offset, 100).stream()
                    .map(this::buildComment)
                    .toList();
                return CommentResponse.from(comment, author, commentLikeCount, replies);
            })
            .toList();
    }

    @Override
    public List<CommentResponse> listReplies(String parentId, String offset, Integer maxSize) {
        return commentPort.listReply(parentId, offset, maxSize).stream()
            .map(this::buildComment)
            .collect(Collectors.toList());
    }

    private boolean equalMetaData(Comment comment, CommentRequest commentRequest) {
        return Objects.equals(comment.getChannelId(), commentRequest.getChannelId()) &&
            Objects.equals(comment.getVideoId(), commentRequest.getVideoId()) &&
            Objects.equals(comment.getParentId(), commentRequest.getParentId());
    }

    private CommentResponse buildComment(Comment comment) {
        var user = loadUserPort.loadUser(comment.getAuthorId())
                .orElse(User.defaultUser(comment.getAuthorId()));
        var commentLikeCount = commentLikePort.getCommentLikeCount(comment.getId());
        return CommentResponse.from(comment, user, commentLikeCount);
    }

//    var replies = commentPort.listReply(parentId, offset, maxSize);
//    var users = getUserMap(replies.stream().map(Comment::getAuthorId).toList());
//
//    private Map<String, User> getUserMap(List<String> userIds) {
//        return loadUserPort.loadAllUsers(userIds).stream()
//            .collect(Collectors.toMap(User::getId, Function.identity()));
//    }
}
