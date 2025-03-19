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
import tnews.subscription.service.UserService;

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

    @Scheduled(fixedRate = 600000) // проверка рассылок новостей раз в 10 минут
    // нужно резать LocalDateTime, так как из-за милисекунд будут пропуски
    public void sendNewsDigest() {
        log.info("Sending news digest");
        List<Subscription> subscriptionList = subscriptionService.findNewsToValidSubscriptions();

        for (Subscription subscription : subscriptionList) {
                Update update = createFakeCallbackUpdate(subscription.getId());
                botController.onUpdateReceived(update);
        }
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
