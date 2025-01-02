package com.example.mytv.adapter.in.api;

import com.example.mytv.adapter.in.api.attribute.HeaderAttribute;
import com.example.mytv.application.port.in.CommentBlockUseCase;
import com.example.mytv.application.port.out.LoadUserPort;
import com.example.mytv.application.port.out.UserSessionPort;
import com.example.mytv.domain.user.User;
import com.example.mytv.domain.user.UserFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentBlockApiController.class)
class CommentBlockApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentBlockUseCase commentBlockUseCase;
    @MockBean
    private UserSessionPort userSessionPort;
    @MockBean
    private LoadUserPort loadUserPort;

    private String authKey;
    private User user;

    @BeforeEach
    void setUp() {
        authKey = UUID.randomUUID().toString();
        user = UserFixtures.stub();

        given(userSessionPort.getUserId(anyString())).willReturn(user.getId());
        given(loadUserPort.loadUser(anyString())).willReturn(Optional.of(user));
    }

    @Test
    void testBlockComment() throws Exception {
        var commentId = "commentId";
        willDoNothing().given(commentBlockUseCase).blockComment(any(), any());

        mockMvc
            .perform(
                post("/api/v1/comments/block?commentId={commentId}", commentId)
                    .header(HeaderAttribute.X_AUTH_KEY, authKey)
            )
            .andExpect(
                status().isOk()
            );

        verify(commentBlockUseCase).blockComment(user, commentId);
    }
}