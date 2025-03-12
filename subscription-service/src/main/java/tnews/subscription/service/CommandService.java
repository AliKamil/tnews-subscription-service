package tnews.subscription.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tnews.aggregator.client.dto.NewsDto;
import tnews.subscription.bot.Command;
import tnews.subscription.bot.KeyboardFactory;
import tnews.subscription.bot.Management;
import tnews.subscription.bot.MessageFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tnews.subscription.entity.Category;
import tnews.subscription.entity.KeyWord;
import tnews.subscription.entity.Subscription;
import tnews.subscription.entity.TimeInterval;
import tnews.subscription.entity.User;
import tnews.subscription.entity.UserAction;

@Slf4j
@Component
@AllArgsConstructor
public class CommandService {
    private final UserService userService;
    private final KeyWordsService keyWordsService;
    private final SubscriptionService subscriptionService;
    private final CategoryService categoryService;


    public List<BotApiMethod<?>> get(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();

        if(message.isCommand()) {
            if (Command.START.getCom().equals(text)) { //TODO: стоит заменить на switch, если добавим меню. Так как там только "команды" (начитаются со /). Просто дублирование методов из handleCallbackQuery
                return start(chatId, message.getFrom().getFirstName(), message.getMessageId());
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
                    return addSubscription(chatId, user, message.getMessageId());
                }
                case UPDATE -> {
                    return updateSubscription(chatId, user);
                }
                case DELETE -> {
                    return exactlyDeleteSubscription(chatId, message.getMessageId());
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
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Command command = null;
        try {
            command = Command.fromString(callbackData.split(" ")[0]);
            log.info(command.getCom());
        } catch (IllegalArgumentException e) {
           log.info(callbackData);
        }

        if (command != null) {
            switch (command) {
                case CATEGORY,
                     ADD_CATEGORY -> {
                    List<Category> categories = categoryService.findAll();
                    return choosingCategories(chatId, messageId, categories);
                }
                case KEYWORD -> {
                    return addKeyWord(chatId, messageId);
                }
                case UPDATE_KEYWORD -> {
                    return updateKeyWord(chatId, messageId);
                }
                case DELETE_KEYWORD -> {
                    return deleteKeyWord(chatId, messageId);
                }
                case UPDATE_CATEGORY -> {
                    return updateCategory(chatId, messageId);
                }
                case DELETE_CATEGORY -> {
                    return deleteCategory(chatId, messageId);
                }
                case DELETE_CATEGORY_ACTION -> {
                    String categoryName = Arrays.stream(callbackData.split(" "))
                            .skip(1)
                            .collect(Collectors.joining(" "));
                    return deleteCategoryAction(chatId, categoryName, messageId);
                }
                case TIME_INTERVAL,
                     UPDATE_TIME_INTERVAL -> {
                    return chooseTimeInterval(chatId, messageId);
                }
                case EXIT -> {
                    return exit(chatId);
                }
                case DELETE -> {
                    return deleteSubscription(chatId, messageId);
                }
                case UPDATE -> {
                    return updateSubscription(chatId, messageId);
                }
                case START -> {
                    return start(chatId, callbackQuery.getFrom().getFirstName(), messageId);
                }
                case CANCELLATION -> {
                    return cancellation(chatId, messageId);
                }
            }
        }
        if (TimeInterval.isEmun(callbackData)) {
            log.info("Update time interval is emun");
            log.info(callbackData);
            return addTimeInterval(chatId, callbackData, messageId);
        }
        if (categoryService.findByCategoryName(callbackData) != null) {
            return addCategory(chatId, callbackData, messageId);
        }
        return List.of(MessageFactory.createMessage(chatId, "Неизвестная команда"));
    }

    private List<BotApiMethod<?>> addSubscription (Long chatId, User user, Integer messageId) {
        if (user.getSubscription() != null) {
            return List.of(
                    MessageFactory.createMessage(chatId, "Подписка уже создана",
                            KeyboardFactory.keyboardMarkup(user.getSubscription())),
                    MessageFactory.createMessage(chatId, "Желаете обновить подписку?",
                            KeyboardFactory.chooseUpdateSubscription())
                    );
        }
        return start(chatId, user.getUsername(), messageId);
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
                        KeyboardFactory.updateButtonsCategoryAndKeyword())
        );
    }

    private List<BotApiMethod<?>> updateSubscription (Long chatId, Integer messageId) {
        User user = userService.findById(chatId);
        if (user.getSubscription() == null) {
            return MessageFactory.createMessage(chatId, "Для начала необходимо создать подписку",
                            KeyboardFactory.createSubscription(), messageId);
        }
        userService.updateCurrentAction(chatId, UserAction.UPDATE.name());
        return MessageFactory.createMessage(chatId, "Что обновить?",
                        KeyboardFactory.updateButtonsCategoryAndKeyword(), messageId);
    }

    private List<BotApiMethod<?>> exactlyDeleteSubscription(Long chatId, Integer messageId) {
        return MessageFactory.createMessage(chatId, "Вы точно хотите удалить подписку?",
                KeyboardFactory.deleteSubscription(), messageId);
    }
    private List<BotApiMethod<?>> deleteSubscription (Long chatId, Integer messageId) {
        User user = userService.findById(chatId);
        if (user.getSubscription() == null) {
            return MessageFactory.createMessage(chatId, "Подписка еще не создана",
                    KeyboardFactory.keyboardMarkup(null), messageId);
        }
        subscriptionService.deleteById(chatId);
        return List.of(
                MessageFactory.createMessage(chatId, "Подписка удалена"),
                MessageFactory.createMessage(chatId, "\uD83D\uDE22",
                        KeyboardFactory.keyboardMarkup(null)),
                DeleteMessage.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .build()
        );
    }

    private List<BotApiMethod<?>> cancellation (Long chatId, Integer messageId) {
        log.info("Delete message {}", messageId);
        return List.of(
                DeleteMessage.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .build()
        );
    }

    private List<BotApiMethod<?>> start (Long chatId, String firstName, Integer messageId) {
        User user = new User();
        user.setId(chatId);
        user.setUsername(firstName);
        user.setCurrentAction(UserAction.START);
        userService.create(user);
        return List.of(
          MessageFactory.createMessage(chatId, "Привет, " + firstName + "! Я новостной бот. Рад тебя видеть!"),
          MessageFactory.createMessage(chatId, "Как будем искать новости? (можно выбрать и категории и ключевые слова)",
                KeyboardFactory.startButtons()),
          DeleteMessage.builder()
                  .chatId(chatId)
                  .messageId(messageId)
                  .build()
        );
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

    private List<BotApiMethod<?>> addKeyWord (Long chatId, Integer messageId) {
        userService.updateCurrentAction(chatId, UserAction.WAITING_FOR_KEYWORD.name());
        return MessageFactory.createMessage(chatId, "Введите одно ключевое слово: ", messageId);
    }

    private List<BotApiMethod<?>> updateKeyWord (Long chatId, Integer messageId) {
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
                  KeyboardFactory.updateKeyWord()),
          DeleteMessage.builder()
                  .chatId(chatId)
                  .messageId(messageId)
                  .build()
        );
    }

    private List<BotApiMethod<?>> deleteKeyWord (Long chatId, Integer messageId) {
        Subscription subscription = subscriptionService.findById(chatId);
        Set<KeyWord> keyWords = subscription.getKeyWords();
        return MessageFactory.createMessage(chatId, "Выбирете ключевое слово для удаления",
                KeyboardFactory.deleteButtonKeyWord(keyWords), messageId);
    }

    private List<BotApiMethod<?>> choosingCategories (Long chatId, Integer messageId, List<Category> categories) {
        return MessageFactory.createMessage(chatId, "Выберите нужную категорию: ",
                KeyboardFactory.categoriesButtons(categories), messageId);
    }

    private List<BotApiMethod<?>> addCategory (Long chatId, String callbackData, Integer messageId) {
        User updateUser = userService.addCategory(chatId, callbackData);
        if (updateUser == null) {
            return MessageFactory.createMessage(chatId, "Пользователь не найден :(", messageId);
        }
        return MessageFactory.createMessage(chatId, "Категория добавлена",
                KeyboardFactory.settingMenu(), messageId);
    }
    private List<BotApiMethod<?>> updateCategory (Long chatId, Integer messageId) {
        Subscription subscription = subscriptionService.findById(chatId);
        Set<Category> categories = subscription.getCategories();
        return List.of(
                MessageFactory.createMessage(chatId, "Ваши категории: "),
                MessageFactory.createMessage(chatId, categories.stream()
                        .map(Category::getCategoryName)
                        .collect(Collectors.joining(", "))),
                MessageFactory.createMessage(chatId, "С чего начнем?", KeyboardFactory.updateCategory()),
                DeleteMessage.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .build()
        );

    }
    private List<BotApiMethod<?>> deleteCategory (Long chatId, Integer messageId) {
        Subscription subscription = subscriptionService.findById(chatId);
        Set<Category> categories = subscription.getCategories();
        return MessageFactory.createMessage(chatId, "Выбирете категорию для удаления",
                KeyboardFactory.deleteButtonsCategory(categories), messageId);
    }

    private List<BotApiMethod<?>> deleteCategoryAction (Long chatId, String categoryName, Integer messageId) {
        Subscription subscription = subscriptionService.findById(chatId);
        Set<Category> categories = subscription.getCategories();
        categories.remove(categoryService.findByCategoryName(categoryName));
        subscription.setCategories(categories);
        subscriptionService.save(subscription);
        return MessageFactory.createMessage(chatId, "Категория удалена "  + categoryName,
            KeyboardFactory.deleteButtonsCategory(categories), messageId);
    }

    private List<BotApiMethod<?>> addTimeInterval (Long chatId, String callbackData, Integer messageId) {
        User user = userService.findById(chatId);
        Subscription subscription = subscriptionService.findById(chatId);
        subscription.setTimeInterval(TimeInterval.valueOf(callbackData));
        subscriptionService.save(subscription);
        switch (user.getCurrentAction()) {
            case READY,
                 START -> {
                return MessageFactory.createMessage(chatId, "Временной интервал успешно добавлен",
                        KeyboardFactory.settingMenu(), messageId);
            }
            case UPDATE -> {
                return MessageFactory.createMessage(chatId, "Временной интервал обнавлен",
                        KeyboardFactory.updateMenu(), messageId);
            }
            default -> {
                return MessageFactory.createMessage(chatId, "Неизвестная команда", messageId);
            }
        }

    }

    private List<BotApiMethod<?>> chooseTimeInterval (Long chatId, Integer messageId) {
        return MessageFactory.createMessage(chatId, "Как часто хотите получать новости?",
                KeyboardFactory.setTimeInterval(), messageId);
    }

    private List<BotApiMethod<?>> exit (Long chatId) {
        Subscription subscription = subscriptionService.findById(chatId);
        if (subscription.getTimeInterval() == null) {
            return List.of(
                    MessageFactory.createMessage(chatId, "Настройка не закончена"),
                    MessageFactory.createMessage(chatId, "Установите частоту обновления новостей",
                            KeyboardFactory.setTimeInterval()));
        }
        userService.updateCurrentAction(chatId, UserAction.READY.name());
        Set<String> categories = subscription.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toSet());
        List<List<NewsDto>> allNews = subscriptionService.getNewsByCategories(categories);

        Set<String> sendNews = subscription.getSentNewsIds();

        List<NewsDto> newNews = allNews.stream()
                .flatMap(Collection::stream)
                .filter(news -> !sendNews.contains(news.getId()))
                .limit(3)
                .toList();

        sendNews.addAll(newNews.stream()
                .map(NewsDto::getId)
                .toList());
        subscription.setSentNewsIds(sendNews);
        subscriptionService.save(subscription);

        return Stream.concat(
                Stream.of(MessageFactory.createMessage(chatId, "Свежие новости")),
                newNews.stream()
                        .map(news -> MessageFactory.createMessage(chatId, news.toString()))
        ).collect(Collectors.toList());
    }

}