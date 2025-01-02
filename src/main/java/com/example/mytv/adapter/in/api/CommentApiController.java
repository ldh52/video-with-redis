package com.example.mytv.adapter.in.api;

import com.example.mytv.adapter.in.api.dto.CommandResponse;
import com.example.mytv.adapter.in.api.dto.CommentRequest;
import com.example.mytv.application.port.in.CommentUseCase;
import com.example.mytv.domain.comment.CommentResponse;
import com.example.mytv.domain.user.User;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentApiController {
    private final CommentUseCase commentUseCase;

    public CommentApiController(CommentUseCase commentUseCase) {
        this.commentUseCase = commentUseCase;
    }

    @PostMapping
    CommandResponse createComment(
        User user,
        @RequestBody CommentRequest commentRequest
    ) {
        var comment = commentUseCase.createComment(user, commentRequest);
        return new CommandResponse(comment.getId());
    }

    @PutMapping("{commentId}")
    CommandResponse updateComment(
        User user,
        @PathVariable String commentId,
        @RequestBody CommentRequest commentRequest
    ) {
        var updateComment = commentUseCase.updateComment(commentId, user, commentRequest);
        return new CommandResponse(updateComment.getId());
    }

    @DeleteMapping("{commentId}")
    void deleteComment(
        User user,
        @PathVariable String commentId
    ) {
        commentUseCase.deleteComment(commentId, user);
    }

    @GetMapping(params = {"commentId"})
    CommentResponse getComment(@RequestParam String commentId) {
        return commentUseCase.getComment(commentId);
    }

    @GetMapping(value = "list", params = {"videoId", "order", "offset", "maxSize"})
    List<CommentResponse> listComments(
        User user,
        @RequestParam String videoId,
        @RequestParam(defaultValue = "time") String order,
        @RequestParam String offset,
        @RequestParam Integer maxSize
    ) {
        return commentUseCase.listComments(user, videoId, order, offset, maxSize);
    }

    @GetMapping(value = "reply", params = {"parentId"})
    List<CommentResponse> listReplyComments(
        @RequestParam String parentId,
        @RequestParam String offset,
        @RequestParam Integer maxSize
    ) {
        return commentUseCase.listReplies(parentId, offset, maxSize);
    }
}
