package com.example.mytv.application.port.out;

import com.example.mytv.domain.channel.Channel;
import java.util.Optional;

public interface LoadChannelPort {
    Optional<Channel> loadChannel(String id);
}
