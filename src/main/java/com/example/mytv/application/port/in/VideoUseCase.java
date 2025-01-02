package com.example.mytv.application.port.in;

import com.example.mytv.adapter.in.api.dto.VideoRequest;
import com.example.mytv.domain.video.Video;
import java.util.List;

public interface VideoUseCase {
    Video getVideo(String videoId);

    List<Video> listVideos(String channelId);

    Video createVideo(VideoRequest videoRequest);

    void increaseViewCount(String videoId);
}
