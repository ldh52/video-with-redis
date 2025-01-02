package com.example.mytv.application;

import com.example.mytv.application.port.in.SubscribeUseCase;
import com.example.mytv.application.port.out.LoadChannelPort;
import com.example.mytv.application.port.out.LoadUserPort;
import com.example.mytv.application.port.out.SubscribePort;
import com.example.mytv.domain.channel.Channel;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService implements SubscribeUseCase {
    private final SubscribePort subscribePort;
    private final LoadChannelPort loadChannelPort;
    private final LoadUserPort loadUserPort;

    public SubscribeService(SubscribePort subscribePort, LoadChannelPort loadChannelPort, LoadUserPort loadUserPort) {
        this.subscribePort = subscribePort;
        this.loadChannelPort = loadChannelPort;
        this.loadUserPort = loadUserPort;
    }

    @Override
    public String subscribeChannel(String channelId, String userId) {
        var channel = loadChannelPort.loadChannel(channelId).get();
        var user = loadUserPort.loadUser(userId).get();

        return subscribePort.insertSubscribeChannel(channel, user);
    }

    @Override
    public void unsubscribeChannel(String subscribeId, String userId) {
        subscribePort.deleteSubscribeChannel(subscribeId);
    }

    @Override
    public List<Channel> listSubscribeChannel(String userId) {
        return subscribePort.listSubscribeChannel(userId);
    }
}
