package com.workonenight.winteambe.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Utils {

    private Utils() {}

    public static boolean checkDateDifference(LocalDateTime date1, LocalDateTime date2, int threshold) {
        return ChronoUnit.DAYS.between(date1, date2) > threshold;
    }

    public static class FirebaseClaims {
        public static final String EMAIL = "email";
        public static final String EMAIL_VERIFIED = "email_verified";
        public static final String NAME = "name";
        public static final String PICTURE = "picture";
        public static final String USER_ID = "user_id";
    }

}
