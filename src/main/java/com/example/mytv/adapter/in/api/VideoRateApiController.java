package com.example.mytv.adapter.in.api;

import com.example.mytv.adapter.in.api.dto.VideoRateResponse;
import com.example.mytv.application.port.in.VideoLikeUseCase;
import com.example.mytv.domain.user.User;
import com.example.mytv.domain.video.VideoRate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/videos/rate")
public class VideoRateApiController {
    private final VideoLikeUseCase videoLikeUseCase;

    public VideoRateApiController(VideoLikeUseCase videoLikeUseCase) {
        this.videoLikeUseCase = videoLikeUseCase;
    }

    @PostMapping
    void rateVideo(
        User user,
        @RequestParam String videoId,
        @RequestParam VideoRate rating
    ) {
        switch (rating) {
            case like:
                videoLikeUseCase.likeVideo(videoId, user.getId());
                break;
            case none:
                videoLikeUseCase.unlikeVideo(videoId, user.getId());
                break;
        }
    }

    @GetMapping
    VideoRateResponse getRate(
        User user,
        @RequestParam String videoId
    ) {
        var rate = videoLikeUseCase.isLikedVideo(videoId, user.getId()) ? VideoRate.like : VideoRate.none;
        return new VideoRateResponse(videoId, rate);
    }
}
