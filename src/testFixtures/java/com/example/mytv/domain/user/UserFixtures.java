package com.example.mytv.domain.user;

public class UserFixtures {
    public static User stub() {
        return User.builder()
            .id("userId")
            .name("name")
            .profileImageUrl("https://example.com/profile.jpg")
            .build();
    }

    public static User stub(String id) {
        return User.builder()
            .id(id)
            .name("name" + id)
            .profileImageUrl("https://example.com/profile.jpg")
            .build();
    }
}
