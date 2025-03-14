package tnews.subscription.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tnews.subscription.bot.Command;
import tnews.subscription.controllers.BotController;
import tnews.subscription.entity.Subscription;
import tnews.subscription.entity.UserAction;
import tnews.subscription.service.CategoryService;
import tnews.subscription.service.SubscriptionService;
import tnews.subscription.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class CategoryUpdateScheduler {
    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final BotController botController;

    @Scheduled(fixedRate = 86400000) // обновление категорий раз в день
    // @Scheduled(fixedRate = 10000) // 10 секунд для тестов
    public void scheduleCategoryUpdate() {
        log.info("Updating categories");
        var categories = categoryService.updateCategories();
        log.info("Updated categories: " + categories.size());
    }

    @Scheduled(fixedRate = 600000) // проверка рассылок новостей раз в минуту для теста оптимально мин время рассылки
    public void sendNewsDigest() {
        log.info("Sending news digest");
        List<Subscription> subscriptionList = subscriptionService.findAll();

        for (Subscription subscription : subscriptionList) {
            if (shouldSend(subscription)) {
                Update update = createFakeCallbackUpdate(subscription.getId());
                botController.onUpdateReceived(update);
            }
        }

    }

    private boolean shouldSend(Subscription subscription) {
        if (userService.findById(subscription.getId()).getCurrentAction().equals(UserAction.UPDATE)) // не очень читабельно, но используется только тут. Стоит ли переписать?
            return false;
        LocalDateTime lastSent = subscription.getLastSend() != null
                ? subscription.getLastSend().truncatedTo(ChronoUnit.MINUTES)
                : null; // скорее всего округление не обязательно, так как отправка раз в час. Но тесты с ним удут более четко
        LocalDateTime now = LocalDateTime.now();
        log.info("Checking if last sent is: {}", lastSent);

        boolean tmp = switch (subscription.getTimeInterval()) {
            case ONE_HOUR -> lastSent == null || lastSent.plusHours(1).isBefore(now); //TODO: пока раз минуту, должно быть раз в час
            case ONE_DAY ->  lastSent == null || lastSent.plusDays(1).isBefore(now);
            case ONE_WEEK -> lastSent == null || lastSent.plusWeeks(1).isBefore(now);
            case ONE_MONTH -> lastSent == null || lastSent.plusMonths(1).isBefore(now);
        };
        subscription.setLastSend(now);
        subscriptionService.save(subscription);
        if (lastSent != null)
            log.info(lastSent.toString());
        else log.info("Last sent is null");
        return tmp;
    }

    /**
     * Создает фейковое нажание на кнопку, которая возвращает команду '/exit'
     * @param chatId - id чата
     * @return - Update объект для BotController
     */
    private Update createFakeCallbackUpdate(Long chatId) {
        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setMessageId(123);
        callbackQuery.setId("fake_callback_id");
        callbackQuery.setMessage(message);
        callbackQuery.setData(Command.EXIT.getCom());
        update.setCallbackQuery(callbackQuery);
        return update;
    }

}
