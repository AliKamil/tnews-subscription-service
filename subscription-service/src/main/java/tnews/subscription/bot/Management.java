package tnews.subscription.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Management {
    ADD("Создать подписку"),
    UPDATE("Обновить подписку"),
    DELETE("Удалить подписку");
    private final String value;

    public static Management fromString(String value) {
        for (Management management : Management.values()) {
            if (management.value.equalsIgnoreCase(value.trim())) {
                return management;
            }
        }
        throw new IllegalArgumentException("Unknown Command: " + value);
    }
}
