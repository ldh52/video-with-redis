package com.example.mytv.adapter.out.redis.channel;

import com.example.mytv.domain.channel.ChannelSnippet;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("channelSnippet")
@AllArgsConstructor
@Getter
public class ChannelSnippetRedisHash {
    private String title;
    private String description;
    private String thumbnailUrl;
    private LocalDateTime publishedAt;

    public static ChannelSnippetRedisHash from(ChannelSnippet snippet) {
        return new ChannelSnippetRedisHash(snippet.getTitle(), snippet.getDescription(), snippet.getThumbnailUrl(), snippet.getPublishedAt());
    }

    public ChannelSnippet toDomain() {
        return ChannelSnippet.builder()
            .title(this.getTitle())
            .description(this.getDescription())
            .thumbnailUrl(this.getThumbnailUrl())
            .publishedAt(this.getPublishedAt())
            .build();
    }
}
