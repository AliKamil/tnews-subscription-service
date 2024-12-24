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
    EXIT("/exit");

    private final String com;


}
