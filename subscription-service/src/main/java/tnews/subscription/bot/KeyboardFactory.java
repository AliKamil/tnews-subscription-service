package tnews.subscription.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import tnews.subscription.entity.Category;
import tnews.subscription.entity.KeyWord;
import tnews.subscription.entity.Subscription;
import tnews.subscription.entity.TimeInterval;

public class KeyboardFactory {

    public static InlineKeyboardMarkup startButtons() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("ПО КАТЕГОРИЯМ", Command.CATEGORY.getCom());
        buttons.put("ПО КЛЮЧЕВЫМ СЛОВАМ", Command.KEYWORD.getCom());
        return createInlineKeyboard(buttons, 2);
    }

    public static InlineKeyboardMarkup updateButtonsCategoryAndKeyword() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("КАТЕГОРИИ", Command.UPDATE_CATEGORY.getCom());
        buttons.put("КЛЮЧЕВЫЕ СЛОВА", Command.UPDATE_KEYWORD.getCom());
        buttons.put("ЧАСТОТА ОБНАВЛЕНИЯ", Command.UPDATE_TIME_INTERVAL.getCom());
        buttons.put("ОТМЕНА", Command.CANCELLATION.getCom() + " " + Management.UPDATE.getValue());
        return createInlineKeyboard(buttons, 2);
    }

    public static InlineKeyboardMarkup settingMenu() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("добавить ключевое слово", Command.KEYWORD.getCom());
        buttons.put("добавить категорию", Command.CATEGORY.getCom());
        buttons.put("добавить вренемой интервал", Command.TIME_INTERVAL.getCom());
        buttons.put("закончить настройку", Command.EXIT.getCom());
        return createInlineKeyboard(buttons, 1);
    }

    public static InlineKeyboardMarkup updateMenu() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("КАТЕГОРИИ", Command.UPDATE_CATEGORY.getCom());
        buttons.put("КЛЮЧЕВЫЕ СЛОВА", Command.UPDATE_KEYWORD.getCom());
        buttons.put("ЧАСТОТА ОБНОВЛЕНИЯ", Command.UPDATE_TIME_INTERVAL.getCom());
        buttons.put("ЗАКОНЧИТЬ НАСТРОЙКУ", Command.EXIT.getCom());
        return createInlineKeyboard(buttons, 1);
    }

    public static InlineKeyboardMarkup deleteButtonsCategory(Set<Category> categories) {
        Map<String, String> buttons = new LinkedHashMap<>();
        for (Category category : categories) {
            buttons.put(category.getCategoryName(), Command.DELETE_CATEGORY_ACTION.getCom() + " " + category.getCategoryName());
        }
        buttons.put("ОТМЕНА", Command.DELETE_CATEGORY.getCom() + " " + Command.CANCELLATION.getCom());
        return createInlineKeyboard(buttons, 3);

    }

    public static InlineKeyboardMarkup categoriesButtons(List<Category> categories) {
        Map<String, String> buttons = new LinkedHashMap<>();
        for (Category category : categories) {
            buttons.put(category.getCategoryName(), category.getCategoryName());
        }
        return createInlineKeyboard(buttons, 3);

    }

    public static InlineKeyboardMarkup updateCategory() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("ДОБАВИТЬ", Command.ADD_CATEGORY.getCom());
        buttons.put("УДАЛИТЬ", Command.DELETE_CATEGORY.getCom());
        buttons.put("ОТМЕНА", Command.CANCELLATION.getCom());
        return createInlineKeyboard(buttons, 2);
    }

    public static InlineKeyboardMarkup updateKeyWord() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("ДОБАВИТЬ", Command.ADD_KEYWORD.getCom());
        buttons.put("УДАЛИТЬ", Command.DELETE_KEYWORD.getCom());
        buttons.put("ОТМЕНА", Command.CANCELLATION.getCom());
        return createInlineKeyboard(buttons, 2);
    }

    public static InlineKeyboardMarkup deleteButtonKeyWord(Set<KeyWord> keyWords) {
        Map<String, String> buttons = new LinkedHashMap<>(); //TODO: либо писать логику для обработки двух ключевых слов, либо изменять UserAction и передавать одно ключевое слово
        for (KeyWord keyWord : keyWords) {
            buttons.put(keyWord.getKeyword(), Command.DELETE_KEYWORD.getCom() + " " + keyWord.getKeyword());
        }
        buttons.put("ОТМЕНА", Command.DELETE_KEYWORD.getCom() + " " + Command.CANCELLATION.getCom());
        return createInlineKeyboard(buttons, 3);
    }

    public static InlineKeyboardMarkup setTimeInterval() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("КАЖДЫЙ ЧАС", TimeInterval.ONE_HOUR.name());
        buttons.put("РАЗ В ДЕНЬ", TimeInterval.ONE_DAY.name());
        buttons.put("КАЖДУЮ НЕДЕЛЮ", TimeInterval.ONE_WEEK.name());
        buttons.put("РАЗ В МЕСЯЦ", TimeInterval.ONE_MONTH.name());
        return createInlineKeyboard(buttons, 2);
    }

    public static InlineKeyboardMarkup createSubscription() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("СОЗДАТЬ ПОДПИСКУ", Command.START.getCom());
        return createInlineKeyboard(buttons, 1);
    }

    public static InlineKeyboardMarkup deleteSubscription() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("ДА", Command.DELETE.getCom());
        buttons.put("НЕТ", Command.EXIT.getCom());
        return createInlineKeyboard(buttons, 2);
    }

    public static InlineKeyboardMarkup chooseUpdateSubscription() {
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("ДА", Command.UPDATE.getCom());
        buttons.put("НЕТ", Command.EXIT.getCom());
        return createInlineKeyboard(buttons, 2);
    }

    private static ReplyKeyboardMarkup replyKeyboardMarkup() {
        List<String> buttons = new ArrayList<>();
        buttons.add(Management.ADD.getValue());
        buttons.add(Management.UPDATE.getValue());
        buttons.add(Management.DELETE.getValue());
        return createReplyKeyboard(buttons);
    }

    private static ReplyKeyboardMarkup replyKeyboardMarkupWithoutCreate() {
        List<String> buttons = new ArrayList<>();
        buttons.add(Management.UPDATE.getValue());
        buttons.add(Management.DELETE.getValue());
        return createReplyKeyboard(buttons);
    }

    public static ReplyKeyboardMarkup keyboardMarkup(Subscription subscription) {
        if (subscription == null) {
            return replyKeyboardMarkup();
        }
        return replyKeyboardMarkupWithoutCreate();
    }

    /**
     * Метод для создания клавиатуры привязанной к сообщению
     *
     * @param buttonData     - данные для создания кнопок (Текст кнопки, Возвращаемое значение кнопки)
     * @param numberOfButton - количество кнопок в строке (желательно не более 3 для коректного отображения на мобильных устройствах)
     * @return
     */
    private static InlineKeyboardMarkup createInlineKeyboard(Map<String, String> buttonData, int numberOfButton) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Map.Entry<String, String> entry : buttonData.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton().builder()
                    .text(entry.getKey())
                    .callbackData(entry.getValue())
                    .build();
            row.add(button);
            if (row.size() == numberOfButton) {
                keyboard.add(row);
                row = new ArrayList<>();
            }
        }
        if (!row.isEmpty()) {
            keyboard.add(row);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Метод для создания повторяющейся клавиатуры. Не привязана к сообщению.
     *
     * @param buttonData - текст кнопки, который будет введен при нажатии на неё.
     * @return
     */
    private static ReplyKeyboardMarkup createReplyKeyboard(List<String> buttonData) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (String button : buttonData) {
            row.add(new KeyboardButton(button));
            if (row.size() == 2) {
                keyboard.add(row);
                row = new KeyboardRow();
            }
        }
        if (!row.isEmpty()) {
            keyboard.add(row);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }


}
