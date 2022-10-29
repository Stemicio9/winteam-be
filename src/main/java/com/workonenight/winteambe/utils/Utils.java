package com.workonenight.winteambe.utils;

import com.workonenight.winteambe.dto.AdvertisementDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
public class Utils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
    public static final String ANONYMOUS = "******";

    private Utils() {
    }

    public static boolean checkDateDifference(LocalDateTime date1, LocalDateTime date2, int threshold) {
        return ChronoUnit.DAYS.between(date1, date2) > threshold;
    }

    public static void checkHourSlot(String hourSlot) {
        if (!hourSlot.equalsIgnoreCase(Days.MATTINA) && !hourSlot.equalsIgnoreCase(Days.POMERIGGIO) && !hourSlot.equalsIgnoreCase(Days.SERA) && !hourSlot.equalsIgnoreCase(Days.NOTTE)) {
            log.error("Hour slot not valid, must be MATTINA, POMERIGGIO, SERA or NOTTE ignoring case");
        }
    }

    public static String calculateAdvertisementStatusDatore(LocalDateTime advertisementDate, String matchedUserId) {
        LocalDateTime now = LocalDateTime.now();
        //active state today is before advertisement date and matched user id is null
        if ((now.isBefore(advertisementDate) || now.isEqual(advertisementDate)) && !StringUtils.hasLength(matchedUserId)) {
            return AdvertisementDatoreState.ACTIVE;
        }
        //accepted state: today is before advertisement date, matched user id is not null
        if (now.isBefore(advertisementDate) && StringUtils.hasLength(matchedUserId)) {
            return AdvertisementDatoreState.ACCEPTED;
        }
        //history stare: adversement date is before today
        if (now.isAfter(advertisementDate)) {
            return AdvertisementDatoreState.HISTORY;
        }
        return AdvertisementDatoreState.ALL;
    }

    public static String calculateAdvertisementStatusLavoratore(AdvertisementDTO advertisement, LocalDateTime advertisementDate, String currentUserId, String matchedUserId) {
        LocalDateTime now = LocalDateTime.now();

        //active state today is before advertisement date and matched user id is null
        if ((now.isBefore(advertisementDate) || now.isEqual(advertisementDate)) && advertisement.getCandidateUserList().contains(currentUserId)) {
            return AdvertisementLavoratoreState.CURRENT;
        }
        //accepted state: today is before advertisement date, matched user id is not null
        if ((now.isBefore(advertisementDate) || now.isEqual(advertisementDate)) && StringUtils.hasLength(matchedUserId) && matchedUserId.equals(currentUserId)) {
            return AdvertisementLavoratoreState.ACCEPTED;
        }
        //history state: advertisement date is before today
        if (now.isAfter(advertisementDate) && (matchedUserId.equals(currentUserId) || advertisement.getCandidateUserList().contains(currentUserId))) {
            return AdvertisementLavoratoreState.HISTORY;
        }
        //ignored state: advertisement date is before today and current user is not matched user and not in candidate list
        if (now.isAfter(advertisementDate) && !matchedUserId.equals(currentUserId) && !advertisement.getCandidateUserList().contains(currentUserId)) {
            return AdvertisementLavoratoreState.IGNORED;
        }
        return AdvertisementLavoratoreState.ALL;
    }

    public static class FirebaseClaims {
        public static final String EMAIL = "email";
        public static final String EMAIL_VERIFIED = "email_verified";
        public static final String NAME = "name";
        public static final String PICTURE = "picture";
        public static final String USER_ID = "user_id";
    }

    public static class AdvertisementDatoreState {
        public static final String ALL = "all";
        public static final String ACTIVE = "active";
        public static final String ACCEPTED = "accepted";
        public static final String HISTORY = "history";
    }

    public static class AdvertisementLavoratoreState {
        public static final String ALL = "all";
        public static final String CURRENT = "current";
        public static final String ACCEPTED = "accepted";
        public static final String HISTORY = "history";
        public static final String IGNORED = "ignored";
    }


}
