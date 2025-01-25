package subscription.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import subscription.bot.Command;
import subscription.bot.KeyboardFactory;
import subscription.bot.Management;
import subscription.bot.MessageFactory;
import subscription.entity.*;

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
            if (Command.START.getCom().equals(text)) { //TODO: стоит заменить на switch, если добавим меню. Так как там только "команды" (начитаются со /). Просто дублирование методов из handleCallbackQuery
                return start(chatId, message.getFrom().getFirstName());
            }
        }

        User user = userService.findById(chatId);

        Management management = null;

        try {
            management = Management.fromString(text);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        if (management != null) {
            switch (management) {
                case ADD -> {
                    return addSubscription(chatId, user);
                }
                case UPDATE -> {
                    return updateSubscription(chatId, user);
                }
                case DELETE -> {
                    return exactlyDeleteSubscription(chatId);
                }
            }
        }

        if(UserAction.WAITING_FOR_KEYWORD.equals(user.getCurrentAction())) {
            return createKeyword(chatId, text);
        }
        return List.of(MessageFactory.createMessage(chatId, "Неизвестная команда",
                        KeyboardFactory.keyboardMarkup(user.getSubscription())));
    }

    public List<BotApiMethod<?>> handleCallbackQuery (Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        Command command = null;
        try {
            command = Command.fromString(callbackData);
        } catch (IllegalArgumentException e) {
            System.out.println(command); //TODO: оставил для настройки, после нужно будет убрать
            System.out.println(callbackData);
        }

        if (command != null) {
            switch (command) {
                case CATEGORY -> {
                    return List.of(MessageFactory.createMessage(chatId, "Выберите нужную категорию: ",
                            KeyboardFactory.categoriesButtons()));
                }
                case KEYWORD -> {
                    return addKeyWord(chatId);
                }
                case UPDATE_KEYWORD -> {
                    return updateKeyWord(chatId);
                }
                case DELETE_KEYWORD -> {
                    return deleteKeyWord(chatId);
                }
                case UPDATE_CATEGORY -> {
                    return updateCategory(chatId);
                }
                case DELETE_CATEGORY -> {
                    return deleteCategory(chatId);
                }
                case TIME_INTERVAL,
                     UPDATE_TIME_INTERVAL -> {
                    return chooseTimeInterval(chatId);
                }

                case EXIT -> {
                    return exit(chatId);
                }
                case DELETE -> {
                    return deleteSubscription(chatId);
                }
                case UPDATE -> {
                    return updateSubscription(chatId);
                }
                case START -> {
                    return start(chatId, callbackQuery.getFrom().getFirstName());
                }

            }
        }
        if (Category1.isEnum(callbackData)) {
            return addCategory(chatId, callbackData);
        }
        if (TimeInterval.isEmun(callbackData)) {
            return addTimeInterval(chatId, callbackData);
        }
        return List.of(MessageFactory.createMessage(chatId, "Неизвестная команда"));
    }

    private List<BotApiMethod<?>> addSubscription (Long chatId, User user) {
        if (user.getSubscription() != null) {
            return List.of(
                    MessageFactory.createMessage(chatId, "Подписка уже создана",
                            KeyboardFactory.keyboardMarkup(user.getSubscription())),
                    MessageFactory.createMessage(chatId, "Желаете обновить подписку?",
                            KeyboardFactory.chooseUpdateSubscription())
                    );
        }
        return start(chatId, user.getUsername());
    }

    private List<BotApiMethod<?>> updateSubscription (Long chatId, User user) {
        if (user.getSubscription() == null) {
            return List.of(
                    MessageFactory.createMessage(chatId, "Для начала необходимо создать подписку",
                            KeyboardFactory.createSubscription()));
        }
        userService.updateCurrentAction(chatId, UserAction.UPDATE.name());
        return List.of(
                MessageFactory.createMessage(chatId, "Что обновить?",
                        KeyboardFactory.updateButtonsCategoryAndKeyword()));
    }

    private List<BotApiMethod<?>> updateSubscription (Long chatId) {
        User user = userService.findById(chatId);
        if (user.getSubscription() == null) {
            return List.of(
                    MessageFactory.createMessage(chatId, "Для начала необходимо создать подписку",
                            KeyboardFactory.createSubscription()));
        }
        userService.updateCurrentAction(chatId, UserAction.UPDATE.name());
        return List.of(MessageFactory.createMessage(chatId, "Что обновить?",
                        KeyboardFactory.updateButtonsCategoryAndKeyword()));
    }

    private List<BotApiMethod<?>> exactlyDeleteSubscription(Long chatId) {
        return List.of(MessageFactory.createMessage(chatId, "Вы точно хотите удалить подписку?",
                KeyboardFactory.deleteSubscription()));
    }
    private List<BotApiMethod<?>> deleteSubscription (Long chatId) {
        User user = userService.findById(chatId);
        if (user.getSubscription() == null) {
            return List.of(MessageFactory.createMessage(chatId, "Подписка еще не создана",
                    KeyboardFactory.keyboardMarkup(null)));
        }
        subscriptionService.deleteById(chatId);
        return List.of(
                MessageFactory.createMessage(chatId, "Подписка удалена"),
                MessageFactory.createMessage(chatId, "\uD83D\uDE22",
                        KeyboardFactory.keyboardMarkup(null))
        );
    }

    private List<BotApiMethod<?>> start (Long chatId, String firstName) {
        User user = new User();
        user.setId(chatId);
        user.setUsername(firstName);
        user.setCurrentAction(UserAction.READY);
        userService.create(user);
        SendMessage firstMessage = MessageFactory.createMessage(chatId,
                "Привет, " + firstName + "! Я новостной бот. Рад тебя видеть!");
        SendMessage secondMessage = MessageFactory.createMessage(chatId,
                "Как будем искать новости? (можно выбрать и категории и ключевые слова)",
                KeyboardFactory.startButtons());
        return List.of(firstMessage, secondMessage);
    }
    private List<BotApiMethod<?>> createKeyword (Long chatId, String keyword) {
        User updateUser = userService.addKeyword(chatId, keyword);
        if (updateUser == null) {
            return List.of(MessageFactory.createMessage(chatId, "Пользователь не найден :("));
        }
        userService.updateCurrentAction(chatId, UserAction.READY.name());
        return List.of(MessageFactory.createMessage(chatId, "Ключевое слово: " + keyword + " добавлено!",
                KeyboardFactory.settingMenu()));
    }

    private List<BotApiMethod<?>> addKeyWord (Long chatId) {
        userService.updateCurrentAction(chatId, UserAction.WAITING_FOR_KEYWORD.name());
        return List.of(MessageFactory.createMessage(chatId, "Введите одно ключевое слово: "));
    }
    private List<BotApiMethod<?>> updateKeyWord (Long chatId) {
        Subscription subscription = subscriptionService.findById(chatId);
        Set<KeyWord> keyWords = subscription.getKeyWords();
        List<String> keyWordsList = new ArrayList<>();
        for (KeyWord keyWord : keyWords) {
            keyWordsList.add(keyWord.getKeyword());
        }
        return List.of(
          MessageFactory.createMessage(chatId, "Ваши ключевые слова:"),
          MessageFactory.createMessage(chatId, keyWordsList.toString()),
          MessageFactory.createMessage(chatId, "С чего начнем?",
                  KeyboardFactory.updateKeyWord())
        );
    }
    private List<BotApiMethod<?>> deleteKeyWord (Long chatId) {
        Subscription subscription = subscriptionService.findById(chatId);
        Set<KeyWord> keyWords = subscription.getKeyWords();
        return List.of(MessageFactory.createMessage(chatId, "Выбирете ключевое слово для удаления",
                KeyboardFactory.deleteButtonKeyWord(keyWords)));
    }

    private List<BotApiMethod<?>> addCategory (Long chatId, String callbackData) {
        User updateUser = userService.addCategory(chatId, callbackData);
        if (updateUser == null) {
            return List.of(MessageFactory.createMessage(chatId, "Пользователь не найден :("));
        }
        return List.of(MessageFactory.createMessage(chatId, "Категория добавлена",
                KeyboardFactory.settingMenu()));
    }
    private List<BotApiMethod<?>> updateCategory (Long chatId) {
        Subscription subscription = subscriptionService.findById(chatId);
        Set<Category> categories = subscription.getCategories();
        List<BotApiMethod<?>> outMsg = new ArrayList<>();
        List<String> categoryNames = new ArrayList<>();
        outMsg.add(MessageFactory.createMessage(chatId, "Ваши категории: "));
        for (Category category : categories) {
            categoryNames.add(category.getCategoryName());
        }
        outMsg.add(MessageFactory.createMessage(chatId, categoryNames.toString()));
        outMsg.add(MessageFactory.createMessage(chatId, "С чего начнем?", KeyboardFactory.updateCategory()));
        return outMsg;
    }
    private List<BotApiMethod<?>> deleteCategory (Long chatId) {
        Subscription subscription = subscriptionService.findById(chatId);
        Set<Category> categories = subscription.getCategories();
        return List.of(MessageFactory.createMessage(chatId, "Выбирете категорию для удаления",
                KeyboardFactory.deleteButtonsCategory(categories)));
    }
    private List<BotApiMethod<?>> addTimeInterval (Long chatId, String callbackData) {
        User user = userService.findById(chatId);
        Subscription subscription = subscriptionService.findById(chatId);
        subscription.setTimeInterval(TimeInterval.valueOf(callbackData));
        subscriptionService.save(subscription);
        switch (user.getCurrentAction()) {
            case READY -> {
                return List.of(MessageFactory.createMessage(chatId, "Временной интервал успешно добавлен",
                        KeyboardFactory.settingMenu()));
            }
            case UPDATE -> {
                return List.of(MessageFactory.createMessage(chatId, "Временной интервал обнавлен",
                        KeyboardFactory.updateMenu()));
            }
            default -> {
                return List.of(MessageFactory.createMessage(chatId, "Неизвестная команда"));
            }
        }

    }

    private List<BotApiMethod<?>> chooseTimeInterval (Long chatId) {
        return List.of(MessageFactory.createMessage(chatId, "Как часто хотите получать новости?",
                KeyboardFactory.setTimeInterval()));
    }

    private List<BotApiMethod<?>> exit (Long chatId) {
        Subscription subscription = subscriptionService.findById(chatId);
        if (subscription.getTimeInterval() == null) {
            return List.of(
                    MessageFactory.createMessage(chatId, "Настройка не закончена"),
                    MessageFactory.createMessage(chatId, "Установите частоту обновления новостей"
                            , KeyboardFactory.setTimeInterval()));
        }
        userService.updateCurrentAction(chatId, UserAction.READY.name());
        return List.of(MessageFactory.createMessage(chatId, "Тут должны появиться первые новости"));
    }

}