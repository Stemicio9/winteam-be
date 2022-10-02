package com.workonenight.winteambe.service.other;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Service
public class FirebaseService {

    //verify firebase token
    public boolean verifyToken(String idToken) {
        try {
            FirebaseAuth.getInstance().verifyIdToken(idToken);
            log.info("Token {} is valid", idToken);
            return true;
        } catch (FirebaseAuthException e) {
            log.error("Error verifying token: {}", e.getMessage());
            return false;
        }
    }

    public User getMinimalUser(HttpServletRequest request) {
        FirebaseToken firebaseToken = getFirebaseToken(request);
        if (firebaseToken != null) {
            User u = new User(firebaseToken.getUid(), firebaseToken.getEmail());
            log.info("Created minimal user: {}", u);
            return u;
        }
        return null;
    }

    //parse all firebase token claims
    public Map<String, Object> parseToken(HttpServletRequest request) {
        FirebaseToken firebaseToken = getFirebaseToken(request);
        if (firebaseToken != null) {
            log.info("Parsed token for user: {}", firebaseToken.getUid());
            return firebaseToken.getClaims();
        }
        log.warn("No token found in request");
        return null;
    }

    public FirebaseToken getFirebaseToken(HttpServletRequest request) {
        String idToken = request.getHeader("w1ntoken");
        if (idToken != null) {
            try {
                return FirebaseAuth.getInstance().verifyIdToken(idToken);
            } catch (FirebaseAuthException e) {
                log.error("Error verifying token: {}", e.getMessage());
                return null;
            }
        }
        return null;
    }
}
