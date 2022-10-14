package com.workonenight.winteambe.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Utils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

    private Utils() {
    }

    public static boolean checkDateDifference(LocalDateTime date1, LocalDateTime date2, int threshold) {
        return ChronoUnit.DAYS.between(date1, date2) > threshold;
    }

    public static String calculateAdvertisementStatus(LocalDateTime advertisementDate, String matchedUserId) {
        LocalDateTime now = LocalDateTime.now();
        //active state today is before advertisement date and matched user id is null
        if ((now.isBefore(advertisementDate) || now.isEqual(advertisementDate)) && matchedUserId == null) {
            return AdvertisementState.ACTIVE;
        }
        //accepted state: today is before advertisement date, matched user id is not null
        if (now.isBefore(advertisementDate) && matchedUserId != null) {
            return AdvertisementState.ACCEPTED;
        }
        //history stare: adversement date is before today
        if (now.isAfter(advertisementDate)) {
            return AdvertisementState.HISTORY;
        }
        return null;
    }

    public static class FirebaseClaims {
        public static final String EMAIL = "email";
        public static final String EMAIL_VERIFIED = "email_verified";
        public static final String NAME = "name";
        public static final String PICTURE = "picture";
        public static final String USER_ID = "user_id";
    }

    public static class AdvertisementState {
        public static final String ALL = "all";
        public static final String ACTIVE = "active";
        public static final String ACCEPTED = "accepted";
        public static final String HISTORY = "history";
    }

}
