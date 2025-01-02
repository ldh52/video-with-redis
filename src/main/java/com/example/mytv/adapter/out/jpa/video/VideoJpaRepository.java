package com.example.mytv.adapter.out.jpa.video;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface VideoJpaRepository extends CrudRepository<VideoJpaEntity, String> {
    List<VideoJpaEntity> findByChannelId(String channelId);
}
