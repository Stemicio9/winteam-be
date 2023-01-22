package com.workonenight.winteambe.service;

import com.workonenight.winteambe.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class UserPermissionService {

    public boolean hasUserSubscription(User user) {
        // todo here we need to check the permission type (number of ads, can i search users)
      return StringUtils.hasLength(user.getSubscriptionName());
    }

}
