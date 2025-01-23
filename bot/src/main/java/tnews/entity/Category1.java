package tnews.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category1 {
    POLITIC("public"),
    SOCIETY("society"),
    ECONOMY("economy"),
    SHOW_BUSINESS("show_business"),
    INCIDENTS("incidents"),
    CULTURE("culture"),
    TECHNOLOGIES("technologies"),
    CAR("car");

    private final String value;

    public static boolean isEnum(String value) {
        for (Category1 e : Category1.values()) {
            if (e.name().equals(value)) {
                return true;
            }
        }
        return false;
    }


}
