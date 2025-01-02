package com.example.mytv.application.schedule;

import com.example.mytv.application.port.out.CacheManagePort;
import com.example.mytv.application.port.out.LoadVideoPort;
import com.example.mytv.application.port.out.SaveVideoPort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VideoViewCountSyncTask {
    private final CacheManagePort cacheManagePort;
    private final LoadVideoPort loadVideoPort;
    private final SaveVideoPort saveVideoPort;

    public VideoViewCountSyncTask(CacheManagePort cacheManagePort, LoadVideoPort loadVideoPort, SaveVideoPort saveVideoPort) {
        this.cacheManagePort = cacheManagePort;
        this.loadVideoPort = loadVideoPort;
        this.saveVideoPort = saveVideoPort;
    }

    @Scheduled(fixedRate = 60000)
    public void syncVideoViewCount() {
        // schedule 동작 확인용
        System.out.println(LocalDateTime.now());

        // 방법 1
//        cacheManagePort.getAllCacheNames(VIDEO_VIEW_COUNT + SEPARATOR)
//            .forEach(key -> {
//                // video:view-count:video1
//                // video:view-count:video2
//                var videoId = key.replace(VIDEO_VIEW_COUNT + SEPARATOR, "");
//                System.out.println("sync:" + videoId);
//
//                saveVideoPort.syncViewCount(videoId);
//            });

        // 방법 2, viewCount 변경이 있는 video 목록만 조회
        loadVideoPort.getAllVideoIdsWithViewCount()
            .forEach(saveVideoPort::syncViewCount);
    }
}
