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
import tnews.subscription.service.CategoryService;
import tnews.subscription.service.SubscriptionService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class CategoryUpdateScheduler {
    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;
    private final BotController botController;

    @Scheduled(fixedRate = 86400000) // обновление категорий раз в день
    // @Scheduled(fixedRate = 10000) // 10 секунд для тестов
    public void scheduleCategoryUpdate() {
        log.info("Updating categories");
        var categories = categoryService.updateCategories();
        log.info("Updated categories: " + categories.size());
    }

    @Scheduled(fixedRate = 60000) // проверка рассылок новостей раз в минуту для теста оптимально мин время рассылки
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
        LocalDateTime lastSent = subscription.getLastSend();
        LocalDateTime now = LocalDateTime.now();

        return switch (subscription.getTimeInterval()) {
            case ONE_HOUR -> lastSent == null || lastSent.plusMinutes(1).isBefore(now); //TODO: пока раз минуту, должно быть раз в час
            case ONE_DAY -> lastSent == null || lastSent.plusDays(1).isBefore(now);
            case ONE_WEEK -> lastSent == null || lastSent.plusWeeks(1).isBefore(now);
            case ONE_MONTH -> lastSent == null || lastSent.plusMonths(1).isBefore(now);
        };
    }

    private Update createFakeCallbackUpdate(Long chatId) {
        Update update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        Message message = new Message();

        // Заполняем сообщение
        message.setChat(new Chat(chatId, "private"));
        message.setMessageId(123); // любое ID

        // Заполняем callbackQuery
        callbackQuery.setId("fake_callback_id");
        callbackQuery.setMessage(message);
        callbackQuery.setData(Command.EXIT.getCom());

        // Добавляем в Update
        update.setCallbackQuery(callbackQuery);
        return update;
    }

}
