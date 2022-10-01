package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class FirebaseService {

    //verify firebase token
    public boolean verifyToken(String idToken) {
        try {
            FirebaseAuth.getInstance().verifyIdToken(idToken);
            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    public User getMinimalUser(HttpServletRequest request) {
        FirebaseToken firebaseToken = getFirebaseToken(request);
        if (firebaseToken != null) {
            return new User(firebaseToken.getUid(), firebaseToken.getEmail());
        }
        return null;
    }

    //parse all firebase token claims
    public Map<String, Object> parseToken(HttpServletRequest request) {
        FirebaseToken firebaseToken = getFirebaseToken(request);
        if (firebaseToken != null) {
            return firebaseToken.getClaims();
        }
        return null;
    }

    public FirebaseToken getFirebaseToken(HttpServletRequest request) {
        String idToken = request.getHeader("w1ntoken");
        if (idToken != null) {
            try {
                return FirebaseAuth.getInstance().verifyIdToken(idToken);
            } catch (FirebaseAuthException e) {
                return null;
            }
        }
        return null;
    }
}
