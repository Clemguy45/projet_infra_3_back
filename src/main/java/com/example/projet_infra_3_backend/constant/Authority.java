package com.example.projet_infra_3_backend.constant;

/**
 * @author Clement Guyot
 * Cette classe sert a faire les constant des Users
 */
public class Authority {
    // les drois des User sur les autres user
    public static final String[] USER_AUTHORITIES = {"user:read"};

    // les drois des Admin sur les autres user
    public static final String[] ADMIN_AUTHORITIES = {"user:read", "user:create" , "user:update", "user:delete"};

}
