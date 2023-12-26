package com.example.projet_infra_3_backend.listener;

import com.example.projet_infra_3_backend.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener {
    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event)  {
        Object principal = event.getAuthentication().getPrincipal();
        // safe check pour voir que c bien un string
        if (principal instanceof String){
            String usernamme = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttemptCache(usernamme);
        }
    }
}
