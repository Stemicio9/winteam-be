package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.enums.FirebaseRequestElement;
import com.workonenight.winteambe.exception.UserNotAuthenticatedException;
import com.workonenight.winteambe.service.other.FirebaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class AuthenticationService {

    private final FirebaseService firebaseService;

    public AuthenticationService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    // todo per v3 e successive
    // todo creare tutte le eccezioni e aggiungere il throw alla firma del metodo


    // This methoid is a subset of getMe, in some cases we need only the user ID
    public String myInfo(HttpServletRequest request, FirebaseRequestElement toSearch) throws UserNotAuthenticatedException {
        FirebaseToken firebaseToken = firebaseService.getFirebaseToken(request);
        if (firebaseToken != null) {
            switch (toSearch) {
                case ID:
                    return firebaseToken.getUid();
                case EMAIL:
                    return firebaseToken.getEmail();
                default:
                    return null;
            }
        } else {
            throw new UserNotAuthenticatedException();
        }
    }

    public User getMinimalUser(HttpServletRequest request) {
        return firebaseService.getMinimalUser(request);
    }


}
