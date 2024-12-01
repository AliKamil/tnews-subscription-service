package tnews.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "NewTNFBot";
    }

    @Override
    public String getBotToken() {
        return "7367963001:AAEKCLGL8Wryxq8AW5E3QlzvHHMK_Iirnjk";
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

}
