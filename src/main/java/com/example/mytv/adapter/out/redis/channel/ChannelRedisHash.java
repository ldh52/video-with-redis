package com.example.mytv.adapter.out.redis.channel;

import com.example.mytv.domain.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import static com.example.mytv.common.CacheNames.CHANNEL;

@RedisHash(value = CHANNEL)
@AllArgsConstructor
@Getter
public class ChannelRedisHash {
    @Id
    private String id;
    private ChannelSnippetRedisHash snippet;
    private ChannelStatisticsRedisHash statistics;
    @Indexed
    private String contentOwnerId;

    public Channel toDomain() {
        return Channel.builder()
            .id(this.getId())
            .snippet(this.getSnippet().toDomain())
            .statistics(this.getStatistics().toDomain())
            .contentOwnerId(this.getContentOwnerId())
            .build();
    }

    public static ChannelRedisHash from(Channel channel) {
        return new ChannelRedisHash(
            channel.getId(),
            ChannelSnippetRedisHash.from(channel.getSnippet()),
            ChannelStatisticsRedisHash.from(channel.getStatistics()),
            channel.getContentOwnerId()
        );
    }
}
