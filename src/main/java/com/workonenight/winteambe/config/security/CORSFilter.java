package com.workonenight.winteambe.config.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {


    String[] whiteListDomains = {"http://192.168.1.74:4200", "http://localhost"};


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;


        // Production
        //         response.setHeader("Access-Control-Allow-Origin", "http://51.38.48.175");
        // Developement

        String currentOrigin = ((HttpServletRequest) req).getHeader("Origin");
        // System.out.println("origin: " + currentOrigin);
        // System.out.println("COOKIE : " + ((HttpServletRequest) req).getHeader("Cookie"));
        if (inWhiteList(currentOrigin)) {
            // System.out.println("PRESENTE IN WHITE LIST");
            response.setHeader("Access-Control-Allow-Origin", currentOrigin);
        } else {
            // System.out.println("NON PRESENTE IN WHITE LIST");
            response.setHeader("Access-Control-Allow-Origin", currentOrigin);
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Headers", "w1ntoken");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("SameSite", "none");

        if (!(request.getMethod().equalsIgnoreCase("OPTIONS"))) {
            try {
                //System.out.println("ESEGUO IL DOFILTER PER IL METODO NON DEL TIPO OPTIONS CORSFILTER");
                chain.doFilter(req, res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //System.out.println("Pre-flight");
            response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type," +
                    "access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with,w1ntoken");
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }


    private boolean inWhiteList(String control) {
        for (String s : whiteListDomains) {
            if (s.equalsIgnoreCase(control)) return true;
        }
        return false;
    }

}
