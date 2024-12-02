package tnews.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class CommandService {

    public SendMessage get(Long chatId, String firstName, String command) {

        if ("/start".equals(command)) {

            return SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Привет, " + firstName + "! Я новостной бот. Рад тебя видеть!").build();
        }
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Неизвестная команда").build();
    }
}