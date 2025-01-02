package com.example.mytv.adapter.in.api;

import com.example.mytv.adapter.in.api.dto.CommandResponse;
import com.example.mytv.application.port.in.SubscribeUseCase;
import com.example.mytv.application.port.in.UserUserCase;
import com.example.mytv.domain.user.User;
import com.example.mytv.domain.channel.Channel;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscribe")
public class ChannelSubscribeApiController {
    private final SubscribeUseCase subscribeUseCase;
    private final UserUserCase userUserCase;

    public ChannelSubscribeApiController(SubscribeUseCase subscribeUseCase, UserUserCase userUserCase) {
        this.subscribeUseCase = subscribeUseCase;
        this.userUserCase = userUserCase;
    }

    @PostMapping
    CommandResponse subscribe(
        User user,
        @RequestParam String channelId
    ) {
        var subscribeId = subscribeUseCase.subscribeChannel(channelId, user.getId());
        return new CommandResponse(subscribeId);
    }

    @DeleteMapping
    void unsubscribe(
        User user,
        @RequestParam String subscribeId
    ) {
        subscribeUseCase.unsubscribeChannel(subscribeId, user.getId());
    }

    @GetMapping("/mine")
    List<Channel> listSubscribeChannelByUser(User user) {
        //var user = userUserCase.getUser(userId);
        return subscribeUseCase.listSubscribeChannel(user.getId());
    }
}


