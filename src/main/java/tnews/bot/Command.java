package tnews.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    START("/start"),
    TEST("/test"),
    CATEGORY("/category"),
    KEYWORD("/keyWord"),
    TIME_INTERVAL("/timeInterval"),
    UPDATE_KEYWORD("/updateKeyword"),
    UPDATE_CATEGORY("/updateCategory"),
    UPDATE_TIME_INTERVAL("/updateTimeInterval"),
    ADD_CATEGORY("/addCategory"),
    ADD_KEYWORD("/addKeyword"),
    DELETE_CATEGORY("/deleteCategory"),
    DELETE_KEYWORD("/deleteKeyword"),
    CANCELLATION("/cancellation"),
    EXIT("/exit");

    private final String com;


}
