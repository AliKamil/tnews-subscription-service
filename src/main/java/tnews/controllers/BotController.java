package tnews.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tnews.service.CommandService;

@Controller
@RequiredArgsConstructor
public class BotController extends TelegramLongPollingBot {
    private final CommandService commandService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String firstName = message.getFrom().getFirstName();
            String text = message.getText();

            try {
                this.execute(commandService.get(chatId, firstName, text));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "tnewsSubscriptionBot";
    }

    @Override
    public String getBotToken() {
        return "8128984956:AAHhuxvs5fGkLYnXT3dhbN7uHsiIF9LiHS0";
    }
}