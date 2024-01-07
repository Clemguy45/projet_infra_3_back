package com.example.projet_infra_3_backend.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.example.projet_infra_3_backend.constant.SecurityConstant.OPTIONS_HTTP_METHOD;
import static com.example.projet_infra_3_backend.constant.SecurityConstant.TOKEN_PREFIX;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JWTTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){
            response.setStatus(HttpStatus.OK.value());
        }else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            // si il n'y a pas bearer ou que le token prefixe est nul on renvoie rien
            if (authorizationHeader==null || !authorizationHeader.startsWith(TOKEN_PREFIX)){
                filterChain.doFilter(request,response);
                return;
            }
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            String username = jwtTokenProvider.getSubject(token);
            // une fois qu'on a viré le bearer on analyse le token , on verifie qu'il est valide, on recupere les roles authortites
            if (jwtTokenProvider.isTokenValid(username,token) && SecurityContextHolder.getContext().getAuthentication() == null){
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                // on peut ensuite creer l'authenticatrion
                Authentication authentication = jwtTokenProvider.getAuthentication(username,authorities,request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request,response);
    }
}
