package com.monitoring.application.channel.controller;

import com.monitoring.application.channel.model.Channel;
import com.monitoring.application.channel.service.ChannelService;
import com.monitoring.application.configuration.OpenApi30Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(OpenApi30Configuration.API_PREFIX + "/channels")
public class ChannelController {
    @Autowired
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }
    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestParam Long chatId) {
        channelService.createChannel(chatId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping(value = "")
    public ResponseEntity<List<Channel>> readAll() {
        final List<Channel> channels = channelService.getAllChannels();
        return channels != null
                ? new ResponseEntity<>(channels, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        final boolean deleted = channelService.deleteChannel(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
