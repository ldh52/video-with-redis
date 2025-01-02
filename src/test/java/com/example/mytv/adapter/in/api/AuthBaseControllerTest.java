package com.example.mytv.adapter.in.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.mytv.application.port.out.LoadUserPort;
import com.example.mytv.application.port.out.UserSessionPort;
import com.example.mytv.domain.user.User;
import com.example.mytv.domain.user.UserFixtures;
import java.util.Optional;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AuthBaseControllerTest {
    @MockBean
    private UserSessionPort userSessionPort;
    @MockBean
    private LoadUserPort loadUserPort;

    private User user;

    void prepareUser() {
        user = UserFixtures.stub();

        given(userSessionPort.getUserId(anyString())).willReturn("userId");
        given(loadUserPort.loadUser(anyString())).willReturn(Optional.of(user));
    }
}
