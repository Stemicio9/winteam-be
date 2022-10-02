package com.workonenight.winteambe.config.security;

import com.google.firebase.auth.FirebaseAuth;
import com.workonenight.winteambe.service.other.FirebaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private FirebaseService firebaseService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String idToken = request.getHeader("w1ntoken");
            //token firebase
            if(idToken != null){
                FirebaseAuth.getInstance().verifyIdToken(idToken);
                filterChain.doFilter(request, response);
            }
        }catch (Exception e){
            log.error("Cannot set user authentication: {}", e);
        }
    }
}
