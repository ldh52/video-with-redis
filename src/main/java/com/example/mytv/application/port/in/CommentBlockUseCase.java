package com.example.mytv.application.port.in;

import com.example.mytv.domain.user.User;

public interface CommentBlockUseCase {
    void blockComment(User user, String commentId);
}
