package tnews.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tnews.entity.Category1;
import tnews.entity.Subscription;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {
    private static InlineKeyboardMarkup createOneButtonInlineKeyboardMarkup(String text, String callback) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

        inlineKeyboardButtons.add(new InlineKeyboardButton().builder()
                .text(text)
                .callbackData(callback)
                .build());
        rows.add(inlineKeyboardButtons);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
    private static InlineKeyboardMarkup createTwoButtonInlineKeyboardMarkup(String textButtonOne, String callbackOne,
                                                                            String textButtonTwo, String callbackTwo) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

        inlineKeyboardButtons.add(new InlineKeyboardButton().builder()
                .text(textButtonOne)
                .callbackData(callbackOne)
                .build());
        inlineKeyboardButtons.add(new InlineKeyboardButton().builder()
                .text(textButtonTwo)
                .callbackData(callbackTwo)
                .build());
        rows.add(inlineKeyboardButtons);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup startButtons() {
        return createTwoButtonInlineKeyboardMarkup("ПО КАТЕГОРИЯМ", Command.CATEGORY.getCom(),
                "ПО КЛЮЧЕВЫМ СЛОВАМ", Command.KEYWORD.getCom());
    }

    public static InlineKeyboardMarkup updateButtonsCategoryAndKeyword() {
        return createTwoButtonInlineKeyboardMarkup("КАТЕГОРИИ", Command.UPDATE_CATEGORY.getCom(),
                "КЛЮЧЕВЫЕ СЛОВА", Command.UPDATE_KEYWORD.getCom());
    }

    public static InlineKeyboardMarkup settingMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons1 = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons2 = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons3 = new ArrayList<>();
        inlineKeyboardButtons.add(new InlineKeyboardButton().builder()
                .text("добавить ключевое слово")
                .callbackData(Command.KEYWORD.getCom())
                .build());
        inlineKeyboardButtons1.add(new InlineKeyboardButton().builder()
                .text("добавить категорию")
                .callbackData(Command.CATEGORY.getCom())
                .build());
        inlineKeyboardButtons2.add(new InlineKeyboardButton().builder()
                .text("добавить вренемой интервал") //TODO: написать попонятнее
                .callbackData(Command.TIME_INTERVAL.getCom())
                .build());
        inlineKeyboardButtons3.add(new InlineKeyboardButton().builder()
                .text("закончить настройку")
                .callbackData(Command.EXIT.getCom())
                .build());
        rows.add(inlineKeyboardButtons);
        rows.add(inlineKeyboardButtons1);
        rows.add(inlineKeyboardButtons2);
        rows.add(inlineKeyboardButtons3);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup categoriesButtons() { //TODO: категории будут подтягиваться с агрегатора (нужно будет переделать)
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        row1.add(new InlineKeyboardButton().builder()
                .text("politic")
                .callbackData(Category1.POLITIC.name())
                .build());
        row1.add(new InlineKeyboardButton().builder()
                .text("society")
                .callbackData(Category1.SOCIETY.name())
                .build());
        row1.add(new InlineKeyboardButton().builder()
                .text("economy")
                .callbackData(Category1.ECONOMY.name())
                .build());
        row1.add(new InlineKeyboardButton().builder()
                .text("show_business")
                .callbackData(Category1.SHOW_BUSINESS.name())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("incidents")
                .callbackData(Category1.INCIDENTS.name())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("culture")
                .callbackData(Category1.CULTURE.name())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("technologies")
                .callbackData(Category1.TECHNOLOGIES.name())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("car")
                .callbackData(Category1.CAR.name())
                .build());
        rows.add(row1);
        rows.add(row2);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup replyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); // подгоряем размер
        replyKeyboardMarkup.setOneTimeKeyboard(false); // скрываем после использования
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add( new KeyboardButton(Management.ADD.getValue()));
        keyboardRow.add( new KeyboardButton(Management.UPDATE.getValue()));
        keyboardRow.add( new KeyboardButton(Management.DELETE.getValue()));
        rows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }
    
    private static ReplyKeyboardMarkup replyKeyboardMarkupWithoutCreate() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add( new KeyboardButton(Management.UPDATE.getValue()));
        keyboardRow.add( new KeyboardButton(Management.DELETE.getValue()));
        rows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup keyboardMarkup(Subscription subscription) {
        if (subscription == null) {
            return replyKeyboardMarkup();
        }
        return replyKeyboardMarkupWithoutCreate();
    }



}
