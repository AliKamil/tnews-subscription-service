package tnews.subscription.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tnews.subscription.service.CommandService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BotController extends TelegramLongPollingBot {

    private final CommandService commandService;

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        List<BotApiMethod<?>> responses = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            responses = commandService.get(update);

        } else if (update.hasCallbackQuery()) {
            responses = commandService.handleCallbackQuery(update);
        }
        if (responses != null) {
            responses.forEach(response -> {
                try {
                    execute(response);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}