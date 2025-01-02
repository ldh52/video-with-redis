package com.example.mytv.adapter.in.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChannelSnippetRequest {
    private String title;
    private String description;
    private String thumbnailUrl;
}
