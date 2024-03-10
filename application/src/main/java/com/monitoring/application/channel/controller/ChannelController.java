package com.monitoring.application.channel.controller;

import com.monitoring.application.channel.model.Channel;
import com.monitoring.application.channel.service.ChannelService;
import com.monitoring.application.configuration.OpenApi30Configuration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Каналы уведомлений", description = "API для каналов уведомлений")
@RestController
@RequestMapping(OpenApi30Configuration.API_PREFIX + "/channels")
public class ChannelController {
    @Autowired
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }
    @Operation(
            summary = "Создать канал уведомлений (только админ)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestParam Long chatId) {
        channelService.createChannel(chatId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Operation(
            summary = "Получить все каналы уведомлений (только админ)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<List<Channel>> readAll() {
        final List<Channel> channels = channelService.getAllChannels();
        return channels != null
                ? new ResponseEntity<>(channels, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Operation(
            summary = "Удалить канал уведомлений (только админ)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        final boolean deleted = channelService.deleteChannel(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
