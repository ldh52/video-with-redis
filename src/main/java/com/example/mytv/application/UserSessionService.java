package com.example.mytv.application;

import com.example.mytv.application.port.in.UserSessionUseCase;
import com.example.mytv.application.port.out.UserSessionPort;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService implements UserSessionUseCase {
    private final UserSessionPort sessionPort;

    public UserSessionService(UserSessionPort sessionPort) {
        this.sessionPort = sessionPort;
    }

    @Override
    public String getUserId(String authKey) {
        return sessionPort.getUserId(authKey);
    }
}
