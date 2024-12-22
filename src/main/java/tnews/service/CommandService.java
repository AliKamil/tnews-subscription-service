package tnews.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tnews.bot.Command;
import tnews.bot.KeyboardFactory;
import tnews.entity.*;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class CommandService {
    private final UserService userService;
    private final KeyWordsService keyWordsService;
    private final SubscriptionService subscriptionService;

    public List<BotApiMethod<?>> get(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();

        if(message.isCommand()) {
            String firstName = message.getFrom().getFirstName();

            if (Command.START.getCom().equals(text)) {
                User user = new User();
                user.setId(chatId);
                user.setUsername(firstName);
                userService.create(user);
                SendMessage firstMessage = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("Привет, " + firstName + "! Я новостной бот. Рад тебя видеть!")
                        .build();
                SendMessage secondMessage = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("Как будем искать навости? (можно выбрать и категории и ключевые слова)")
                        .replyMarkup(KeyboardFactory.startButtons())
                        .build();
                return List.of(firstMessage, secondMessage);
            }
        }   //TODO: пока обрабатывает только /start. Тут должно быть управление подпиской

        User user = userService.findById(chatId);
        if(UserAction.WAITING_FOR_KEYWORD.equals(user.getCurrentAction())) {
            User updateUser = userService.addKeyword(chatId, text);
            if (updateUser == null) {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text("Пользователь не найден :(")
                        .build());
            }
            return List.of(SendMessage.builder()
                    .chatId(chatId)
                    .text("Ключевое слово: " + text + " добавлено!")
                    .replyMarkup(KeyboardFactory.keyWordButton())
                    .build());
        }

        return List.of(SendMessage.builder()
                .chatId(chatId.toString())
                .text("Неизвестная команда")
                .build());
    }

    public BotApiMethod<?> handleCallbackQuery (Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        if (Command.CATEGORY.getCom().equals(callbackData)) {
            return SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Выберите нужную категорию: ")
                    .replyMarkup(KeyboardFactory.categoriesButtons())
                    .build();
        } else if (Command.KEYWORD.getCom().equals(callbackData)) {
            userService.updateCurrentAction(chatId, UserAction.WAITING_FOR_KEYWORD.name());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Введите ключевое слово: ")
                    .build();
        } else if (Category1.isEnum(callbackData)) {
            User updateUser = userService.addCategory(chatId, callbackData);
            if (updateUser == null) {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text("Пользователь не найден :(")
                        .build();
            }
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Категория добавлена. :) Можете выбрать еще несколько категорий")
                    .build();
            
        }

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Неизвестная команда")
                .build();
    }

}