package com.monitoring.application.telegram;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
@Component(value = "notificationbot")
public class NotificationBot extends AbilityBot {
    private final ResponseHandler responseHandler;
    public NotificationBot(Environment environment) {
        super(environment.getProperty("BOT_TOKEN"), environment.getProperty("BOT_USERNAME"));
        responseHandler = new ResponseHandler(silent);
    }
    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info("Starts the bot")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> responseHandler.sendMessage(ctx.chatId(),"Hello"))
                .build();
    }
    public void sendMessageToChannel(Long chatId, String message){
        responseHandler.sendMessage(chatId,message);
    }
    @Override
    public long creatorId() {
        return 1L;
    }
}
