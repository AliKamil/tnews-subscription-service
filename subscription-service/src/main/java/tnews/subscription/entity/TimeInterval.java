package tnews.subscription.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeInterval {
    ONE_HOUR("ONE_HOUR"),
    ONE_DAY("ONE_DAY"),
    ONE_WEEK("ONE_WEEK"),
    ONE_MONTH("ONE_MONTH");
    private String value;

    public static boolean isEmun(String timeInterval) {
        for (TimeInterval t : TimeInterval.values()) {
            if (t.name().equals(timeInterval)) {
                return true;
            }
        }
        return false;
    }

}
