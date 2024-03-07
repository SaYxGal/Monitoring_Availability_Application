package com.monitoring.application.channel.service;

import com.monitoring.application.channel.model.Channel;

import java.util.List;

public interface ChannelService {
    List<Channel> getAllChannels();
    Channel findChannel(Long id);
    void createChannel(Long chatId);
    Boolean deleteChannel(Long id);
}
