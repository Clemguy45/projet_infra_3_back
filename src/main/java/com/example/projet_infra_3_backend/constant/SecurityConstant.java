package com.example.projet_infra_3_backend.constant;

/**
 * @author Clement Guyot
 * Cette classe Entropose toute les constantes liée au token JWT pour la sécurité de springboot
 */
public class SecurityConstant {
    public static final String TOKEN_PREFIX = "Bearer "; //ownership , no need more checks
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String AUTHORITIES = "Authorities";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String [] PUBLIC_URLS= {"/fiche/liste/**", "/user/login" , "/user/register/**","/user/image/**", "/swagger-ui/**", "/v2/api-docs","/swagger-resources/**", "/webjars/**","projet-infra-3-backend-dev.azurewebsites.net/**", "/user/**"};
}
