package com.example.mytv.application.port.out;

import com.example.mytv.domain.channel.Channel;

public interface SaveChannelPort {
    void saveChannel(Channel channel);
}
