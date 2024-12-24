package tnews.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Management {
    ADD("Создать подписку"),
    UPDATE("Обновить подписку"),
    DELETE("Удалить подписку");
    private final String value;
}
