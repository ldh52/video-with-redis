package com.example.mytv.adapter.in.api.dto;

import com.example.mytv.domain.video.VideoRate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VideoRateResponse {
    private String videoId;
    private VideoRate rate;
}
