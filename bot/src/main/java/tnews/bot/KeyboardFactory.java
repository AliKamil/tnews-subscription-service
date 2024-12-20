package tnews.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tnews.entity.Category1;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {
    public static InlineKeyboardMarkup startButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        inlineKeyboardButtons.add(new InlineKeyboardButton().builder()
                .text("CATEGORY")
                .callbackData(Command.CATEGORY.getCom())
                .build());
        inlineKeyboardButtons.add(new InlineKeyboardButton().builder()
                .text("KEYWORDS")
                .callbackData(Command.KEYWORD.getCom())
                .build());
        rows.add(inlineKeyboardButtons);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup categoriesButtons() {
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

}
