package tnews.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tnews.bot.Command;
import tnews.bot.KeyboardFactory;
import tnews.entity.Category1;
import tnews.entity.Subscription;
import tnews.entity.User;
import tnews.mapper.UserMapper;

import java.util.List;

@Component
@AllArgsConstructor
public class CommandService {
    private final UserService userService;

    public List<BotApiMethod<?>> get(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        if(message.isCommand()) {
            String firstName = message.getFrom().getFirstName();
            String command = message.getText();

            if (Command.START.getCom().equals(command)) {
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
                        .text("Choose...")
                        .replyMarkup(KeyboardFactory.startButtons())
                        .build();
                return List.of(firstMessage, secondMessage);
            }
        }   //TODO: пока обрабатывает только /start. Тут должно быть управление подпиской
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
                    .text("Choose you category:")
                    .replyMarkup(KeyboardFactory.categoriesButtons())
                    .build();
        } else if (Command.KEYWORD.getCom().equals(callbackData)) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Some keywords choosing for you ! ! !")
                    .build();
        } else if (Category1.isEnum(callbackData)) {
            userService.addCategory(chatId, callbackData);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Category added :) You can choose more")
                    .build();
            
        }

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Error :(")
                .build();
    }

}