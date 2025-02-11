package tnews.subscription.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tnews.subscription.controllers.BotController;

@Component
@Slf4j
public class BotInitializer {

    private final TelegramBotsApi telegramBotsApi;
    private final BotController botController;

    public BotInitializer(BotController botController) throws TelegramApiException {
        this.telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        this.botController = botController;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        telegramBotsApi.registerBot(botController);
    }
}