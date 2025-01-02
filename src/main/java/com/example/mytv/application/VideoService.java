package com.example.mytv.application;

import com.example.mytv.adapter.in.api.dto.VideoRequest;
import com.example.mytv.application.port.in.VideoUseCase;
import com.example.mytv.application.port.out.*;
import com.example.mytv.domain.video.Video;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class VideoService implements VideoUseCase {
    private final LoadVideoPort loadVideoPort;
    private final SaveVideoPort saveVideoPort;
    private final VideoLikePort videoLikePort;
    private final LoadChannelPort loadChannelPort;
    private final SaveChannelPort saveChannelPort;
    private final MessagePort messagePort;

    public VideoService(LoadVideoPort loadVideoPort, SaveVideoPort saveVideoPort, VideoLikePort videoLikePort, LoadChannelPort loadChannelPort, SaveChannelPort saveChannelPort, MessagePort messagePort) {
        this.loadVideoPort = loadVideoPort;
        this.saveVideoPort = saveVideoPort;
        this.videoLikePort = videoLikePort;
        this.loadChannelPort = loadChannelPort;
        this.saveChannelPort = saveChannelPort;
        this.messagePort = messagePort;
    }

    @Override
    public Video getVideo(String videoId) {
        var video = loadVideoPort.loadVideo(videoId);
        var viewCount = loadVideoPort.getViewCount(videoId);
        var likeCount = videoLikePort.getVideoLikeCount(videoId);
        video.bindCount(viewCount, likeCount);

        return video;
    }

    @Override
    public List<Video> listVideos(String channelId) {
        return loadVideoPort.loadVideoByChannel(channelId).stream()
            .map(video -> {
                var viewCount = loadVideoPort.getViewCount(video.getId());
                var likeCount = videoLikePort.getVideoLikeCount(video.getId());
                video.bindCount(viewCount, likeCount);
                return video;
            })
            .toList();
    }

    @Override
    public Video createVideo(VideoRequest videoRequest) {
        var video = Video.builder()
                .id(UUID.randomUUID().toString())
                .title(videoRequest.getTitle())
                .description(videoRequest.getDescription())
                .thumbnailUrl(videoRequest.getThumbnailUrl())
                .channelId(videoRequest.getChannelId())
                .publishedAt(LocalDateTime.now())
                .build();
        saveVideoPort.saveVideo(video);

        var channel = loadChannelPort.loadChannel(videoRequest.getChannelId()).get();
        channel.getStatistics().updateVideoCount(channel.getStatistics().getVideoCount() + 1);
        saveChannelPort.saveChannel(channel);

        messagePort.sendNewVideMessage(video.getChannelId());

        return video;
    }

    @Override
    public void increaseViewCount(String videoId) {
        saveVideoPort.incrementViewCount(videoId);
    }
}
