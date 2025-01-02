package com.example.mytv.adapter.in.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Getter
public class CommentRequest {
    private String channelId;
    private String videoId;
    private String parentId;
    private String text;
}
