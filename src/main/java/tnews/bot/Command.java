package tnews.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    START("/start"),
    TEST("/test"),
    CATEGORY("/category"),
    KEYWORD("/keyWord");

    private final String com;


}
