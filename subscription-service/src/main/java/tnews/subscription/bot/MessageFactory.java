package tnews.subscription.bot;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

public class MessageFactory {
    /**
     * Создает сообщение без кнопок
     * @param id - id получателя сообщения
     * @param text - текст сообщения
     * @return
     */
    public static SendMessage createMessage (Long id, String text) {
        return SendMessage.builder()
                .chatId(id)
                .text(text)
                .build();
    }

    /**
     * Создает сообщение с кнопками
     * @param id - id получателя сообщения
     * @param text - текст сообщения
     * @param replyMarkup - возвращаемое значение при нажатии кнопки
     * @return
     */
    public static SendMessage createMessage (Long id, String text, ReplyKeyboard replyMarkup) {
        return SendMessage.builder()
                .chatId(id)
                .text(text)
                .replyMarkup(replyMarkup)
                .build();
    }

    /**
     * Создает сообщение без кнопок c удалением прошлого сообщения
     * @param id - id получателя сообщения
     * @param text - текст сообщения
     * @param messageId - id сообщение которое будет удалено
     * @return
     */
    public static List<BotApiMethod<?>> createMessage (Long id, String text, Integer messageId) {
        return List.of(
                createMessage(id, text),
                DeleteMessage.builder()
                        .chatId(id)
                        .messageId(messageId)
                        .build()
                );
    }

    /**
     * Создает сообщение с кнопками c удалением прошлого сообщения
     * @param id - id получателя сообщения
     * @param text - текст сообщения
     * @param replyMarkup - возвращаемое значение при нажатии кнопки
     * @param messageId - id сообщение которое будет удалено
     * @return
     */
    public static List<BotApiMethod<?>> createMessage (Long id, String text, ReplyKeyboard replyMarkup, Integer messageId) {
        return List.of(
                createMessage(id, text, replyMarkup),
                DeleteMessage.builder()
                        .chatId(id)
                        .messageId(messageId)
                        .build()
        );
    }
}
