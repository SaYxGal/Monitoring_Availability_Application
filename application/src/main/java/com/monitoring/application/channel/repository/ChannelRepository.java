package com.monitoring.application.channel.repository;

import com.monitoring.application.channel.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}
