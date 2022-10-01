package com.workonenight.winteambe.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Utils {

    private Utils() {}

    public static boolean checkDateDifference(LocalDateTime date1, LocalDateTime date2, int threshold) {
        return ChronoUnit.DAYS.between(date1, date2) > threshold;
    }

}
