package tnews.subscription.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    START("/start"),
    DELETE("/delete"),
    UPDATE("/update"),
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
    DELETE_TIME_INTERVAL("/deleteTimeInterval"),
    CANCELLATION("/cancellation"),
    EXIT("/exit");

    private final String com;

    public static Command fromString(String com) {
        for (Command command : Command.values()) {
            if (command.com.equalsIgnoreCase(com.trim())) {
                return command;
            }
        }
        throw new IllegalArgumentException("Unknown Command: " + com);
    }

}
