package com.example.mytv.adapter.out.jpa.subscribe;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SubscribeJpaRepository extends CrudRepository<SubscribeJpaEntity, String> {
    List<SubscribeJpaEntity> findAllByUserId(String userId);
    List<SubscribeJpaEntity> findAllByChannelId(String channelId);
}
