package com.example.mytv.adapter.in.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChannelRequest {
    private ChannelSnippetRequest snippet;
    private String contentOwnerId;
}
