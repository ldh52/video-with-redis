package com.example.mytv.domain.channel;

import com.example.mytv.adapter.in.api.dto.ChannelSnippetRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Channel {
    private String id;
    private ChannelSnippet snippet;
    private ChannelStatistics statistics;
    private String contentOwnerId;

    public void updateSnippet(ChannelSnippetRequest snippetRequest) {
        this.snippet = ChannelSnippet.builder()
            .title(snippetRequest.getTitle())
            .description(snippetRequest.getDescription())
            .thumbnailUrl(snippetRequest.getThumbnailUrl())
            .build();
    }
}
