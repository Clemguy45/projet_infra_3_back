package com.example.projet_infra_3_backend.config;

import static com.example.projet_infra_3_backend.constant.Authority.ADMIN_AUTHORITIES;
import static com.example.projet_infra_3_backend.constant.Authority.USER_AUTHORITIES;

/**
 * @author Clement Guyot
 * Cette classe Enum tout les roles des users
 */
public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);


    private String[] authorities;

    /**
     * Constructors of class Role
     * @param authorities
     */
    Role(String... authorities){
        this.authorities = authorities;
    }

    /**
     * Get tout les Roles possibles des user
     * @return la list des authorities des users
     */
    public String[] getAuthorities() {
        return authorities;
    }
}
