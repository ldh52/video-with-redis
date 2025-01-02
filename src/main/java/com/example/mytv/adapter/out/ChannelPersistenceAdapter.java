package com.example.mytv.adapter.out;

import com.example.mytv.adapter.out.jpa.channel.ChannelJpaEntity;
import com.example.mytv.adapter.out.jpa.channel.ChannelJpaRepository;
import com.example.mytv.adapter.out.redis.channel.ChannelRedisHash;
import com.example.mytv.adapter.out.redis.channel.ChannelRedisRepository;
import com.example.mytv.application.port.out.LoadChannelPort;
import com.example.mytv.application.port.out.SaveChannelPort;
import com.example.mytv.domain.channel.Channel;
import com.example.mytv.domain.channel.ChannelSnippet;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ChannelPersistenceAdapter implements LoadChannelPort, SaveChannelPort {
    private final ChannelJpaRepository channelJpaRepository;
    private final ChannelRedisRepository channelRedisRepository;

    public ChannelPersistenceAdapter(ChannelJpaRepository channelJpaRepository, ChannelRedisRepository channelRedisRepository) {
        this.channelJpaRepository = channelJpaRepository;
        this.channelRedisRepository = channelRedisRepository;
    }

    @Override
    public void saveChannel(Channel channel) {
        channelRedisRepository.deleteById(channel.getId());
        channelRedisRepository.save(ChannelRedisHash.from(channel));

        channelJpaRepository.save(ChannelJpaEntity.from(channel));
    }

    @Override
    public Optional<Channel> loadChannel(String id) {
        return channelRedisRepository.findById(id)
                // redis cache hit
                .map(ChannelRedisHash::toDomain)
                .or(() -> {
                    // redis cache miss
                    var optionalEntity = channelJpaRepository.findById(id);
                    optionalEntity.ifPresent(jpaEntity -> channelRedisRepository.save(ChannelRedisHash.from(jpaEntity.toDomain())));

                    return optionalEntity.map(ChannelJpaEntity::toDomain);
                });
    }
}
