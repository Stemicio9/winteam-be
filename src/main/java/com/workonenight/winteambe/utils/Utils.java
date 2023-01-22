package com.workonenight.winteambe.utils;


import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.dto.response.SubscriptionResponse;
import com.workonenight.winteambe.entity.Advertisement;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.entity.annotations.anonymous.AnonymizedObject;
import com.workonenight.winteambe.entity.annotations.anonymous.InterceptingMethods;
import com.workonenight.winteambe.entity.interfaces.Anonymizable;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import com.workonenight.winteambe.entity.interfaces.Transferrable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Utils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final String ANONYMOUS = "******";

    public static boolean checkDateDifference(LocalDateTime date1, LocalDateTime date2, int threshold) {
        return ChronoUnit.DAYS.between(date1, date2) > threshold;
    }

    public static void checkHourSlot(String hourSlot) {
        if (!hourSlot.equalsIgnoreCase(Days.MATTINA) &&
                !hourSlot.equalsIgnoreCase(Days.POMERIGGIO) &&
                !hourSlot.equalsIgnoreCase(Days.SERA) &&
                !hourSlot.equalsIgnoreCase(Days.NOTTE)) {
            log.error("Hour slot not valid, must be MATTINA, POMERIGGIO, SERA or NOTTE ignoring case");
        }
    }

    private static String calculateAdvertisementStatus(String hourSlot, LocalDateTime advertisementDate, String matchedUserId) {
        LocalDateTime now = LocalDateTime.now();
        //active state today is before advertisement date and matched user id is null
        if ((beforeDateWithHourSlot(hourSlot, advertisementDate) || now.isEqual(advertisementDate)) && !StringUtils.hasLength(matchedUserId)) {
            return AdvertisementDatoreState.ACTIVE;
        }
        //accepted state: today is before advertisement date, matched user id is not null
        if (beforeDateWithHourSlot(hourSlot, advertisementDate) && StringUtils.hasLength(matchedUserId)) {
            return AdvertisementDatoreState.ACCEPTED;
        }
        //history stare: adversement date is before today
        if (afterDateWithHourSlot(hourSlot, advertisementDate)) {
            return AdvertisementDatoreState.HISTORY;
        }
        return AdvertisementDatoreState.ALL;
    }

    private static String calculateAdvertisementStatus(String hourSlot, LocalDateTime advertisementDate, String matchedUserId, String currentUserId, List<User> candidateUserList) {
        LocalDateTime now = LocalDateTime.now();

        //active state today is before advertisement date and matched user id is null
        if ((beforeDateWithHourSlot(hourSlot, advertisementDate) || now.isEqual(advertisementDate)) && candidateUserList.contains(currentUserId)) {
            return AdvertisementLavoratoreState.CURRENT;
        }
        //accepted state: today is before advertisement date, matched user id is not null
        if ((beforeDateWithHourSlot(hourSlot, advertisementDate) || now.isEqual(advertisementDate)) && StringUtils.hasLength(matchedUserId) && matchedUserId.equals(currentUserId)) {
            return AdvertisementLavoratoreState.ACCEPTED;
        }
        //history state: advertisement date is before today
        if (afterDateWithHourSlot(hourSlot, advertisementDate) && (matchedUserId.equals(currentUserId) || candidateUserList.contains(currentUserId))) {
            return AdvertisementLavoratoreState.HISTORY;
        }
        //ignored state: advertisement date is before today and current user is not matched user and not in candidate list
        if (afterDateWithHourSlot(hourSlot, advertisementDate) && !matchedUserId.equals(currentUserId) && !candidateUserList.contains(currentUserId)) {
            return AdvertisementLavoratoreState.IGNORED;
        }
        return AdvertisementLavoratoreState.ALL;
    }

    public static String evaluateStatus(Advertisement advertisement, String currentUserId, String role) {
        User matchedUser = advertisement.getMatchedUser();
        String matchedUserId = matchedUser != null ? matchedUser.getId() : "";
        if (role.equals("DATORE")) {
            return calculateAdvertisementStatus(advertisement.getHourSlot(), advertisement.getDate(), matchedUserId);
        } else {
            return calculateAdvertisementStatus(advertisement.getHourSlot(), advertisement.getDate(), matchedUserId, currentUserId, advertisement.getCandidateUserList());
        }
    }


    @AnonymizedObject
    public static DataTransferObject toDto(Transferrable transferrable, boolean anonymize) {
        DataTransferObject base = transferrable.asDTO().createBaseDTO();
        Class dtoClass = base.getClass();
        Class entityClass = transferrable.getClass();
        for (Field field : dtoClass.getDeclaredFields()) {
            Field that = null;
            try {
                field.setAccessible(true);
                that = entityClass.getDeclaredField(field.getName());
                that.setAccessible(true);
                field.set(base, that.get(transferrable));
            } catch (IllegalAccessException e) {
                log.error("Error while converting to DTO field {}", field.getName());
            } catch (NoSuchFieldException ignored) {

            } catch (IllegalArgumentException e) {
                try {
                    cheRiceve(that.getType(), field.getType(), field.get(base), that.get(transferrable));
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (anonymize && base instanceof Anonymizable) {
            return (DataTransferObject) InterceptingMethods.validate((Anonymizable) base);
        }
        return base;
    }

    @AnonymizedObject
    public static DataTransferObject toDto(Transferrable transferrable) {
        return toDto(transferrable, true);
    }

    @AnonymizedObject
    public static DataTransferObject toDto(Transferrable transferrable, String currentUser, String currentRole) {
        DataTransferObject base = toDto(transferrable, true);
        if (transferrable instanceof Advertisement) {
            Advertisement advertisement = (Advertisement) transferrable;
            AdvertisementDTO advertisementDTO = (AdvertisementDTO) base;
            advertisementDTO.setAdvertisementStatus(evaluateStatus(advertisement, currentUser, currentRole));
            //     base.setExtraField("advertisementStatus", evaluateStatus(advertisement, currentUser, currentRole));
        }
        return base;
    }

    public static void cheRiceve(Class classEntity, Class classDto, Object initField, Object copyField) {
        Field[] fields = classDto.getDeclaredFields();
        for (Field field : fields) {
            Field that = null;
            try {
                field.setAccessible(true);
                that = classEntity.getDeclaredField(field.getName());
                that.setAccessible(true);
                field.set(initField, that.get(copyField));
            } catch (NoSuchFieldException e) {
                log.error("Non riesco a prendere il field {}", field.getName());
            } catch (IllegalAccessException e) {
                log.error("Error while converting to DTO field {}", field.getName());
            } catch (IllegalArgumentException e) {
                try {
                    cheRiceve(that.getType(), field.getType(), field.get(initField), that.get(copyField));
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static boolean beforeDateWithHourSlot(String hourSlot, LocalDateTime advertisementDate) {
        LocalDateTime now = LocalDateTime.now();
        if (hourSlot.equalsIgnoreCase(Days.MATTINA)) {
            return now.isBefore(advertisementDate.withHour(12).withMinute(0).withSecond(0));
        }
        if (hourSlot.equalsIgnoreCase(Days.POMERIGGIO)) {
            return now.isBefore(advertisementDate.withHour(18).withMinute(0).withSecond(0));
        }
        if (hourSlot.equalsIgnoreCase(Days.SERA)) {
            return now.isBefore(advertisementDate.withHour(23).withMinute(59).withSecond(59));
        }
        if (hourSlot.equalsIgnoreCase(Days.NOTTE)) {
            return now.isBefore(advertisementDate.withHour(6).withMinute(0).withSecond(0));
        }
        return false;
    }

    public static int fromHourSlotToHour(String hourSlot) {
        if (hourSlot.equalsIgnoreCase(Days.MATTINA)) {
            return 12;
        }
        if (hourSlot.equalsIgnoreCase(Days.POMERIGGIO)) {
            return 18;
        }
        if (hourSlot.equalsIgnoreCase(Days.SERA)) {
            return 23;
        }
        if (hourSlot.equalsIgnoreCase(Days.NOTTE)) {
            return 6;
        }
        return -1;
    }

    public static boolean afterDateWithHourSlot(String hourSlot, LocalDateTime advertisementDate) {
        return !beforeDateWithHourSlot(hourSlot, advertisementDate);
    }


    public static Page<DataTransferObject> convertPage(Page<? extends Transferrable> page) {
        return new PageImpl(page.getContent().stream().map(Utils::toDto).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());

    }

    public static Page<DataTransferObject> convertPage(Page<? extends Transferrable> page, String currentUser, String currentRole) {
        return new PageImpl(page.getContent().stream().map(element -> toDto(element, currentUser, currentRole)).collect(Collectors.toList()), page.getPageable(), page.getContent().size());
    }

    public static SubscriptionResponse composeSubscriptionResponse(User user) {
        return new SubscriptionResponse(
                user.getSubscriptionName(),
                user.getImageLink(),
                user.getAdvertisementLeft(),
                user.getExpiringSubscriptionDate(),
                user.isSearchEnabled(),
                user.isCreateAdvertisementEnabled());
    }


    public static Query stateToQuery(Query query, String state, String role, String currentUserId) {
        LocalDateTime now = LocalDateTime.now();
        if (role.equals("DATORE")) {
            if (state.equals(AdvertisementDatoreState.ACTIVE)) {
                query.addCriteria(Criteria.where("date").gte(now));
                query.addCriteria(Criteria.where("matchedUser").exists(false));
            } else if (state.equals(AdvertisementDatoreState.ACCEPTED)) {
                query.addCriteria(Criteria.where("date").gte(now));
                query.addCriteria(Criteria.where("matchedUser").exists(true));
            } else if (state.equals(AdvertisementDatoreState.HISTORY)) {
                query.addCriteria(Criteria.where("date").lte(now));
            }
        } else {
            if(!state.equals(AdvertisementLavoratoreState.SEARCH)) {
                query.addCriteria(Criteria.where("candidateUserList").elemMatch(Criteria.where("$id").is(currentUserId)));
            }
            if(state.equals(AdvertisementLavoratoreState.SEARCH)) {
                query.addCriteria(Criteria.where("date").gte(now));
            }
            if (state.equals(AdvertisementLavoratoreState.CURRENT)) {
                query.addCriteria(Criteria.where("date").gte(now));
                query.addCriteria(Criteria.where("matchedUser.$id").ne(currentUserId));
            } else if (state.equals(AdvertisementLavoratoreState.ACCEPTED)) {
                query.addCriteria(Criteria.where("date").gte(now));
                query.addCriteria(Criteria.where("matchedUser.$id").is(currentUserId));
            } else if (state.equals(AdvertisementLavoratoreState.HISTORY)) {
                query.addCriteria(Criteria.where("date").lte(now));
            }
        }
        query.with(Sort.by(Sort.Direction.DESC, "date"));
        return query;
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

        public static final String SEARCH = "search";
    }

}
