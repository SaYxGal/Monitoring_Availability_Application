package com.monitoring.application.telegram;

import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ResponseHandler {
    private final SilentSender sender;

    public ResponseHandler(SilentSender sender) {
        this.sender = sender;
    }
    public void sendMessage(long chatId, String message) {
        SendMessage newMessage = new SendMessage();
        newMessage.setChatId(chatId);
        newMessage.setText(message);
        sender.execute(newMessage);
    }
}
