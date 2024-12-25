package tnews.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tnews.entity.Category;
import tnews.entity.Category1;
import tnews.entity.Subscription;
import tnews.entity.TimeInterval;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KeyboardFactory {

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
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

        inlineKeyboardButtons.add(new InlineKeyboardButton().builder()
                .text("ПО КАТЕГОРИЯМ")
                .callbackData(Command.CATEGORY.getCom())
                .build());
        inlineKeyboardButtons.add(new InlineKeyboardButton().builder()
                .text("ПО КЛЮЧЕВЫМ СЛОВАМ")
                .callbackData(Command.KEYWORD.getCom())
                .build());
        rows.add(inlineKeyboardButtons);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup updateButtonsCategoryAndKeyword() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row1.add(new InlineKeyboardButton().builder()
                .text("КАТЕГОРИИ")
                .callbackData(Command.UPDATE_CATEGORY.getCom())
                .build());
        row1.add(new InlineKeyboardButton().builder()
                .text("КЛЮЧЕВЫЕ СЛОВА")
                .callbackData(Command.UPDATE_KEYWORD.getCom())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("ЧАСТОТА ОБНАВЛЕНИЯ") //TODO: написать попонятнее
                .callbackData(Command.UPDATE_TIME_INTERVAL.getCom())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("ОТМЕНА")
                .callbackData(Command.CATEGORY.getCom() + " " + Management.UPDATE.getValue())
                .build());
        rows.add(row1);
        rows.add(row2);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
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

    public static InlineKeyboardMarkup deleteButtonsCategory(Set<Category> categories) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        if (categories.size() < 3) {
            for (Category category : categories) {
                row1.add(new InlineKeyboardButton().builder()
                        .text(category.getCategoryName())
                        .callbackData(Command.DELETE_CATEGORY.getCom() + " " + category.getCategoryName())
                        .build());
            }
        } else {
            int i = 0;

            for (Category category : categories) {
                if (i < 3) {
                    row1.add(new InlineKeyboardButton().builder()
                            .text(category.getCategoryName())
                            .callbackData(Command.DELETE_CATEGORY.getCom() + " " + category.getCategoryName())
                            .build());
                    i++;
                } else if (i >= 3 && i < 6) {
                    row2.add(new InlineKeyboardButton().builder()
                            .text(category.getCategoryName())
                            .callbackData(Command.DELETE_CATEGORY.getCom() + " " + category.getCategoryName())
                            .build());
                    i++;
                } else if (i >= 6) { //TODO: нужно будет подогнать под количество категорий
                    row3.add(new InlineKeyboardButton().builder()
                            .text(category.getCategoryName())
                            .callbackData(Command.DELETE_CATEGORY.getCom() + " " + category.getCategoryName())
                            .build());
                    i++;
                }
            }
        }
        row4.add(new InlineKeyboardButton().builder()
                .text("ОТМЕНА")
                .callbackData(Command.CANCELLATION.getCom() + " " + Command.DELETE_CATEGORY.getCom())
                .build());
        rows.add(row1);
        if (!row2.isEmpty())
            rows.add(row2);
        if (!row3.isEmpty())
            rows.add(row3);
        rows.add(row4);
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

    public static InlineKeyboardMarkup updateCategory() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        row1.add(new InlineKeyboardButton().builder()
                .text("ДОБАВИТЬ")
                .callbackData(Command.ADD_CATEGORY.getCom())
                .build());
        row1.add(new InlineKeyboardButton().builder()
                .text("УДАЛИТЬ")
                .callbackData(Command.DELETE_CATEGORY.getCom())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("ОТМЕНА")     //TODO: "ОТМЕНА" может быть не совсем понятна, стоит переименовать?!
                .callbackData(Command.CANCELLATION.getCom()) //TODO: скорее всего стоит объединить с завершение настройти (сделать EXIT)
                .build());
        rows.add(row1);
        rows.add(row2);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup updateKeyWord() {
        return createTwoButtonInlineKeyboardMarkup("ДОБАВИТЬ", Command.ADD_KEYWORD.getCom(),
                "УДАЛИТЬ", Command.DELETE_KEYWORD.getCom()); // TODO: переделать как категории
    }

    public static InlineKeyboardMarkup setTimeInterval() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        row1.add(new InlineKeyboardButton().builder()
                .text("КАЖДЫЙ ЧАС")
                .callbackData(TimeInterval.ONE_HOUR.name())
                .build());
        row1.add(new InlineKeyboardButton().builder()
                .text("РАЗ В ДЕНЬ")
                .callbackData(TimeInterval.ONE_DAY.name())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("КАЖДУЮ НЕДЕЛЮ")
                .callbackData(TimeInterval.ONE_WEEK.name())
                .build());
        row2.add(new InlineKeyboardButton().builder()
                .text("РАЗ В МЕСЯЦ")
                .callbackData(TimeInterval.ONE_MONTH.name())
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
