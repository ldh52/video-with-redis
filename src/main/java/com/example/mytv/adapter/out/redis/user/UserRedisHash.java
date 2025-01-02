package com.example.mytv.adapter.out.redis.user;

import com.example.mytv.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import static com.example.mytv.common.CacheNames.USER;

@RedisHash(value = USER)
@AllArgsConstructor
@Getter
public class UserRedisHash {
    private String id;
    private String name;
    private String profileImageUrl;

    public static UserRedisHash from(User user) {
        return new UserRedisHash(user.getId(), user.getName(), user.getProfileImageUrl());
    }

    public User toDomain() {
        return User.builder()
            .id(this.getId())
            .name(this.getName())
            .profileImageUrl(this.getProfileImageUrl())
            .build();
    }
}
