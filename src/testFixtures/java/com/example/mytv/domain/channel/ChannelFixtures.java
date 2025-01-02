package com.example.mytv.domain.channel;

public class ChannelFixtures {
    public static Channel stub(String id) {
        return Channel.builder()
            .id(id)
            .snippet(ChannelSnippetFixtures.stub())
            .statistics(ChannelStatisticsFixtures.stub())
            .contentOwnerId("user")
            .build();
    }
}
