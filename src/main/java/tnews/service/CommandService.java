package tnews.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tnews.bot.*;
import tnews.entity.*;

import java.util.ArrayList;
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
                SendMessage firstMessage = MessageFactory.createMessage(chatId,
                        "Привет, " + firstName + "! Я новостной бот. Рад тебя видеть!");
                SendMessage secondMessage = MessageFactory.createMessage(chatId,
                        "Как будем искать новости? (можно выбрать и категории и ключевые слова)",
                        KeyboardFactory.startButtons());
                return List.of(firstMessage, secondMessage);
            }

        }   //TODO: пока обрабатывает только /start. Тут должно быть управление подпиской. Стоит заменить на switch?!

        User user = userService.findById(chatId);
        if(UserAction.WAITING_FOR_KEYWORD.equals(user.getCurrentAction())) {
            User updateUser = userService.addKeyword(chatId, text);
            if (updateUser == null) {
                return List.of(MessageFactory.createMessage(chatId, "Пользователь не найден :("));
            }
            return List.of(MessageFactory.createMessage(chatId, "Ключевое слово: " + text + " добавлено!",
                            KeyboardFactory.settingMenu()));
        }

        if (Management.ADD.getValue().equals(text)) {
            if (user.getSubscription() != null) {
                return List.of(MessageFactory.createMessage(chatId, "Подписка уже создана",
                                KeyboardFactory.updateButtonsCategoryAndKeyword()));
            }
            return List.of(MessageFactory.createMessage(chatId, "Введите: " + Command.START.getCom()));
        }

        if (Management.UPDATE.getValue().equals(text)) {
            if (user.getSubscription() == null) {
                return List.of(
                        MessageFactory.createMessage(chatId, "Для начала необходимо создать подписку"),
                        MessageFactory.createMessage(chatId, "Введите: " + Command.START.getCom()));
            }
            return List.of(
                    MessageFactory.createMessage(chatId, "Что обновить?",
                            KeyboardFactory.updateButtonsCategoryAndKeyword()));
        }
        if (Management.DELETE.getValue().equals(text)) {
            //TODO: нужно реализовать!!!
        }

        return List.of(MessageFactory.createMessage(chatId, "Неизвестная команда",
                        KeyboardFactory.keyboardMarkup(user.getSubscription())));
    }

    public List<BotApiMethod<?>> handleCallbackQuery (Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        if (Command.EXIT.getCom().equals(callbackData)) {
            Subscription subscription = subscriptionService.findById(chatId);
            if (subscription.getTimeInterval() == null) {
                return List.of(
                        MessageFactory.createMessage(chatId, "Настройка не закончена"),
                        MessageFactory.createMessage(chatId, "Установите частоту обновления новостей"
                            , KeyboardFactory.setTimeInterval()));
            }
            return List.of(MessageFactory.createMessage(chatId, "Тут должны появиться первые новости")); //TODO: НОВОСТИ ОТ АГРЕГАТОРА
        }
        if (Command.CATEGORY.getCom().equals(callbackData) || Command.ADD_CATEGORY.getCom().equals(callbackData)) { //TODO: нужно что-то вроде proxy для разного ответа
            return List.of(MessageFactory.createMessage(chatId, "Выберите нужную категорию: ",
                            KeyboardFactory.categoriesButtons()));

        } else if (Command.KEYWORD.getCom().equals(callbackData)) {
            userService.updateCurrentAction(chatId, UserAction.WAITING_FOR_KEYWORD.name());
            return List.of(MessageFactory.createMessage(chatId, "Введите одно ключевое слово: "));

        } else if (Category1.isEnum(callbackData)) {
            User updateUser = userService.addCategory(chatId, callbackData);
            if (updateUser == null) {
                return List.of(MessageFactory.createMessage(chatId, "Пользователь не найден :("));
            }
            return List.of(MessageFactory.createMessage(chatId, "Категория добавлена",
                    KeyboardFactory.settingMenu()));

            
        } else if (Command.UPDATE_CATEGORY.getCom().equals(callbackData)) {
            Subscription subscription = subscriptionService.findById(chatId);
            Set<Category> categories = subscription.getCategories();
            List<BotApiMethod<?>> outMsg = new ArrayList<>();
            outMsg.add(MessageFactory.createMessage(chatId, "Ваши категории: "));
            for (Category category : categories) {
                outMsg.add(MessageFactory.createMessage(chatId, category.getCategoryName()));
            }
            outMsg.add(MessageFactory.createMessage(chatId, "С чего начнем?", KeyboardFactory.updateCategory()));
            return outMsg;
        } else if (Command.DELETE_CATEGORY.getCom().equals(callbackData)) {
            Subscription subscription = subscriptionService.findById(chatId);
            Set<Category> categories = subscription.getCategories();
            return List.of(MessageFactory.createMessage(chatId, "Выбирете категорию для удаления",
                    KeyboardFactory.deleteButtonsCategory(categories)));
        } else if (Command.TIME_INTERVAL.getCom().equals(callbackData)) {
            return List.of(MessageFactory.createMessage(chatId, "Как часто хотите получать новости?",
                    KeyboardFactory.setTimeInterval()));
        } else if (TimeInterval.isEmun(callbackData)) {
            Subscription subscription = subscriptionService.findById(chatId);
            subscription.setTimeInterval(TimeInterval.valueOf(callbackData));
            subscriptionService.save(subscription);
            return List.of(MessageFactory.createMessage(chatId, "Временной интервал успешно добавлен",
                    KeyboardFactory.settingMenu()));
        }

        return List.of(MessageFactory.createMessage(chatId, "Неизвестная команда"));
    }

}