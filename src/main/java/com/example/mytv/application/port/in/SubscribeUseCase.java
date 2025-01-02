package com.example.mytv.application.port.in;

import com.example.mytv.domain.channel.Channel;
import java.util.List;

public interface SubscribeUseCase {
    String subscribeChannel(String channelId, String userId);

    void unsubscribeChannel(String subscribeId, String userId);

    List<Channel> listSubscribeChannel(String userId);
}
