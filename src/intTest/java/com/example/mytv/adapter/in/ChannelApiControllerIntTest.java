package com.example.mytv.adapter.in;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mytv.adapter.in.api.dto.ChannelRequest;
import com.example.mytv.adapter.in.api.dto.ChannelSnippetRequest;
import com.example.mytv.adapter.out.jpa.channel.ChannelJpaRepository;
import com.example.mytv.adapter.out.jpa.user.UserJpaEntity;
import com.example.mytv.adapter.out.jpa.user.UserJpaRepository;
import com.example.mytv.config.TestRedisConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = TestRedisConfig.class)
@AutoConfigureMockMvc
public class ChannelApiControllerIntTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ChannelJpaRepository channelJpaRepository;

    @Test
    @DisplayName("POST /api/v1/channels")
    void testPostChannel() throws Exception {
        // Given
        userJpaRepository.save(new UserJpaEntity("user", "user", "https://exmaple.com/profile.jpg"));
        var body = new ChannelRequest(new ChannelSnippetRequest("channel", "desc", "https://exmaple.com/image.jpg"), "user");

        // When
        mockMvc
            .perform(
                post("/api/v1/channels")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(body))
            )
            .andExpect(
                status().isOk()
            );

        // Then
        then(channelJpaRepository.count())
            .isEqualTo(1L);
    }
}
