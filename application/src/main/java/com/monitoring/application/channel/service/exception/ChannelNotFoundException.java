package com.monitoring.application.channel.service.exception;

public class ChannelNotFoundException extends RuntimeException{
    public ChannelNotFoundException(Long id) {
        super(String.format("Channel with id [%s] is not found", id));
    }
}
