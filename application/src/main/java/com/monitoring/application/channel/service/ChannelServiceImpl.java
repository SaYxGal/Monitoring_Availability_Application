package com.monitoring.application.channel.service;

import com.monitoring.application.channel.model.Channel;
import com.monitoring.application.channel.repository.ChannelRepository;
import com.monitoring.application.channel.service.exception.ChannelNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelServiceImpl implements ChannelService{
    private final ChannelRepository channelRepository;

    public ChannelServiceImpl(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Channel findChannel(Long id) {
        final Optional<Channel> channel = channelRepository.findById(id);
        return channel.orElseThrow(() -> new ChannelNotFoundException(id));
    }

    @Override
    public void createChannel(Long chatId) {
        final Channel channel = new Channel(chatId);
        channelRepository.save(channel);
    }

    @Override
    public Boolean deleteChannel(Long id) {
        try {
            channelRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
