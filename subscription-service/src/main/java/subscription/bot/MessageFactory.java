package subscription.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class MessageFactory {
    public static SendMessage createMessage (Long id, String text) {
        return SendMessage.builder()
                .chatId(id)
                .text(text)
                .build();
    }

    public static SendMessage createMessage (Long id, String text, ReplyKeyboard replyMarkup) {
        return SendMessage.builder()
                .chatId(id)
                .text(text)
                .replyMarkup(replyMarkup)
                .build();
    }
}
