package com.example.mytv.domain.comment;

import java.time.LocalDateTime;

public class CommentFixtures {
    public static Comment stub(String id) {
        return Comment.builder()
            .id(id)
            .channelId("channelId")
            .videoId("videoId")
            .text("comment")
            .authorId("userId")
            .publishedAt(LocalDateTime.now())
            .build();
    }
}
