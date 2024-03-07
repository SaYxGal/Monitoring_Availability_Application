package com.monitoring.application.channel.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channel_seq")
    @SequenceGenerator(name = "channel_seq", sequenceName = "CHANNEL_SEQ", allocationSize = 1)
    private Long id;
    @Column(nullable = false, name = "chat_id")
    private Long chatId;

    public Channel() {
    }

    public Channel(Long chatId) {
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id) && Objects.equals(chatId, channel.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", chatId=" + chatId +
                '}';
    }
}
