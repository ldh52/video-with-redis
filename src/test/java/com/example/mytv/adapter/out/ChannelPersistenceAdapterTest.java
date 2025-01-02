package com.example.mytv.adapter.out;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.mytv.adapter.out.jpa.channel.ChannelJpaEntity;
import com.example.mytv.adapter.out.jpa.channel.ChannelJpaEntityFixtures;
import com.example.mytv.adapter.out.jpa.channel.ChannelJpaRepository;
import com.example.mytv.adapter.out.redis.channel.ChannelRedisHash;
import com.example.mytv.adapter.out.redis.channel.ChannelRedisRepository;
import com.example.mytv.domain.channel.ChannelFixtures;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ChannelPersistenceAdapterTest {
    private ChannelPersistenceAdapter sut;

    private final ChannelJpaRepository channelJpaRepository = mock(ChannelJpaRepository.class);
    private final ChannelRedisRepository channelRedisRepository = mock(ChannelRedisRepository.class);

    @BeforeEach
    void setUp() {
        sut = new ChannelPersistenceAdapter(channelJpaRepository, channelRedisRepository);
    }

    @Nested
    @DisplayName("saveChannel")
    class SaveChannelTest {
        @Test
        @DisplayName("Channel 을 Jpa Repository 에 저장")
        void saveChannel() {
            // Given
            var channel = ChannelFixtures.stub("channelId");

            // When
            sut.saveChannel(channel);

            // Then
            var argumentCaptor = ArgumentCaptor.forClass(ChannelJpaEntity.class);
            verify(channelJpaRepository).save(argumentCaptor.capture());
            then(argumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("id", channel.getId())
                .hasFieldOrPropertyWithValue("snippet.title", channel.getSnippet().getTitle());
            verify(channelRedisRepository).deleteById("channelId");
        }
    }

    @Nested
    @DisplayName("loadChannel")
    class LoadChannelTest {
        @Test
        @DisplayName("Redis 에서 Channel 을 찾을 수 있으면 RedisHash 에서 Channel 반환")
        void existRedis_returnRedisRepository() {
            var id = "channelId";
            given(channelRedisRepository.findById(any()))
                .willReturn(Optional.of(ChannelRedisHash.from(ChannelFixtures.stub(id))));

            var result = sut.loadChannel(id);

            then(result)
                .isPresent()
                .hasValueSatisfying(channel -> then(channel)
                    .hasFieldOrPropertyWithValue("id", id));
        }

        @Test
        @DisplayName("Redis 에서 Channel 을 찾을 수 없으면 Jpa 에서 Channel 반환")
        void notExistRedis_returnJpaRepository() {
            var id = "channelId";
            given(channelRedisRepository.findById(any()))
                .willReturn(Optional.empty());
            given(channelJpaRepository.findById(any()))
                .willReturn(Optional.of(ChannelJpaEntityFixtures.stub(id)));

            var result = sut.loadChannel(id);

            then(result)
                .isPresent()
                .hasValueSatisfying(channel -> then(channel)
                    .hasFieldOrPropertyWithValue("id", id));
        }

        @Test
        @DisplayName("Redis, Jpa 에서 Channel 을 찾을 수 없으면 empty Optional 반환")
        void notExist_returnEmpty() {
            var id = "channelId";
            given(channelRedisRepository.findById(any()))
                .willReturn(Optional.empty());
            given(channelJpaRepository.findById(any()))
                .willReturn(Optional.empty());

            var result = sut.loadChannel(id);

            then(result)
                .isNotPresent();
        }
    }
}