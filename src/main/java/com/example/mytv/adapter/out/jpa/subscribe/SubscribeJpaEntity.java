package com.example.mytv.adapter.out.jpa.subscribe;

import com.example.mytv.adapter.out.jpa.channel.ChannelJpaEntity;
import com.example.mytv.adapter.out.jpa.user.UserJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscribe")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SubscribeJpaEntity {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private ChannelJpaEntity channel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;
}
