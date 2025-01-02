package com.example.mytv.adapter.in.dto;

import com.example.mytv.adapter.in.api.dto.VideoRequest;

public class VideoRequestFixtures {
    public static VideoRequest stub() {
        return new VideoRequest(
            "title",
            "desc",
            "https://example.com/image.jpg",
            "channelId"
        );
    }
}
