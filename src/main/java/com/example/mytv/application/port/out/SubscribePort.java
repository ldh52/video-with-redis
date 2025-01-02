package com.example.mytv.application.port.out;

import com.example.mytv.domain.channel.Channel;
import com.example.mytv.domain.user.User;
import java.util.List;

public interface SubscribePort {
    String insertSubscribeChannel(Channel channel, User user);

    void deleteSubscribeChannel(String subscribeId);

    List<Channel> listSubscribeChannel(String userId);
}
