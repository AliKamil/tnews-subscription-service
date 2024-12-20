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
                        .text("Choose...")
                        .replyMarkup(KeyboardFactory.startButtons())
                        .build();
                return List.of(firstMessage, secondMessage);
            }
        }   //TODO: пока обрабатывает только /start. Тут должно быть управление подпиской

        User user = userService.findById(chatId);
        if(UserAction.WAITING_FOR_KEYWORD.equals(user.getCurrentAction())) {
            KeyWord keyWord = new KeyWord();
            keyWord.setKeyword(text);
            keyWordsService.saveKeyWord(keyWord);

            Subscription subscription = user.getSubscription();
            if (subscription == null) {
                subscription = new Subscription();
                subscription.setId(chatId);
            }
            if (subscription.getKeyWords() == null || subscription.getKeyWords().isEmpty()) {
                subscription.setKeyWords(Set.of(keyWord));
            } else subscription.getKeyWords().add(keyWord);

            subscriptionService.save(subscription);

            user.setSubscription(subscription);
            user.setCurrentAction(UserAction.READY);
            userService.create(user);

            return List.of(SendMessage.builder()
                    .chatId(chatId)
                    .text("Key word: " + keyWord.getKeyword() + " added to subscription!")
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
                    .text("Choose you category:")
                    .replyMarkup(KeyboardFactory.categoriesButtons())
                    .build();
        } else if (Command.KEYWORD.getCom().equals(callbackData)) {
            userService.updateCurrentAction(chatId, UserAction.WAITING_FOR_KEYWORD.name());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Enter the keyword:")
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