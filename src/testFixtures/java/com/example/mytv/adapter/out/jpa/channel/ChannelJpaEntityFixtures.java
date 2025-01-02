package com.example.mytv.adapter.out.jpa.channel;

import java.time.LocalDateTime;

public class ChannelJpaEntityFixtures {
    public static ChannelJpaEntity stub(String id) {
        return new ChannelJpaEntity(
            id,
            new ChannelSnippetJpaEntity("title", "description", "https://example.com/thumbnail", LocalDateTime.now()),
            new ChannelStatisticsJpaEntity(10L, 10L, 10L),
            "user"
        );
    }
}
