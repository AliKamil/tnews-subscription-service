package tnews.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import tnews.bot.Command;
import tnews.bot.KeyboardFactory;
import tnews.bot.Management;
import tnews.entity.*;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class CommandService {
    private final UserService userService;
    private final KeyWordsService keyWordsService;
    private final SubscriptionService subscriptionService;

    private SendMessage createMessage (Long id, String text) {
        return SendMessage.builder()
                .chatId(id)
                .text(text)
                .build();
    }

    private SendMessage createMessageWithReplyMarkup (Long id, String text, ReplyKeyboard replyMarkup) {
        return SendMessage.builder()
                .chatId(id)
                .text(text)
                .replyMarkup(replyMarkup)
                .build();
    }

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
                SendMessage firstMessage = createMessage(chatId,
                        "Привет, " + firstName + "! Я новостной бот. Рад тебя видеть!");
                SendMessage secondMessage = createMessageWithReplyMarkup(chatId,
                        "Как будем искать новости? (можно выбрать и категории и ключевые слова)",
                        KeyboardFactory.startButtons());
                return List.of(firstMessage, secondMessage);
            }
            if (Command.UPDATE_CATEGORY.getCom().equals(text)) {
                Subscription subscription = subscriptionService.findById(chatId);
                Set<Category> categories = subscription.getCategories();
            }
        }   //TODO: пока обрабатывает только /start. Тут должно быть управление подпиской. Стоит заменить на switch?!

        User user = userService.findById(chatId);
        if(UserAction.WAITING_FOR_KEYWORD.equals(user.getCurrentAction())) {
            User updateUser = userService.addKeyword(chatId, text);
            if (updateUser == null) {
                return List.of(createMessage(chatId, "Пользователь не найден :("));
            }
            return List.of(createMessageWithReplyMarkup(chatId, "Ключевое слово: " + text + " добавлено!",
                            KeyboardFactory.settingMenu()));
        }
        if (Management.ADD.getValue().equals(text)) {
            if (user.getSubscription() != null) {
                return List.of(createMessageWithReplyMarkup(chatId, "Подписка уже создана",
                                KeyboardFactory.updateButtonsCategoryAndKeyword()));
            }
            return List.of( createMessage(chatId, "Введите: " + Command.START.getCom()));
        }

        if (Management.UPDATE.getValue().equals(text)) {
            if (user.getSubscription() == null) {
                return List.of(
                        createMessage(chatId, "Для начала необходимо создать подписку"),
                        createMessage(chatId, "Введите: " + Command.START.getCom()));
            }
            return List.of(
                    createMessageWithReplyMarkup(chatId, "Что обновить?",
                            KeyboardFactory.updateButtonsCategoryAndKeyword()));
        }
        if (Management.DELETE.getValue().equals(text)) {
            //TODO: нужно реализовать!!! может заменить на switch? (Management)
        }

        return List.of(createMessageWithReplyMarkup(chatId, "Неизвестная команда",
                        KeyboardFactory.keyboardMarkup(user.getSubscription())));
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
                    .text("Введите одно ключевое слово: ")
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
                    .text("Категория добавлена")
                    .replyMarkup(KeyboardFactory.settingMenu())
                    .build();
            
        }

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Неизвестная команда")
                .build();
    }

}