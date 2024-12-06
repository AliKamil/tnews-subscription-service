package tnews.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tnews.bot.Command;

@Component
public class CommandService {

    public SendMessage get(Long chatId, String firstName, String command) {

        if (Command.START.getCom().equals(command)) {
            System.out.println("chatId: " + chatId + " firstName: " + firstName + " command: " + command);
            return SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Привет, " + firstName + "! Я новостной бот. Рад тебя видеть!").build();
        }
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Неизвестная команда").build();
    }
}