package com.workonenight.winteambe.utils;

public class UserType {

    private UserType() {}
    public static final String ADMIN = "ADMIN";
    public static final String DATORE = "DATORE";
    public static final String LAVORATORE = "LAVORATORE";
    public static final String INFLUENCER = "INFLUENCER";

    //verify if string matches one role
    public static boolean isRole(String role) {
        return role.equals(ADMIN) || role.equals(DATORE) || role.equals(LAVORATORE) || role.equals(INFLUENCER);
    }
}
