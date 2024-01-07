package com.example.projet_infra_3_backend.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author Clement Guyot
 * Cette classe de type Service permet de savoir combien d'essaie un utilisateur à avant d'être bloquer
 */
@Service
public class LoginAttemptService {
    public static final int MAXIMUM_NUMBER_OF_ATTEMPTS=5;
    public static final int ATTEMPT_INCREMENT=1;
    private LoadingCache<String,Integer> loginAttemptCache;


    // on initialise le cache via la libraire google
    public LoginAttemptService(){
        super();
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });

    }
    public void evictUserFromLoginAttemptCache(String username){
        loginAttemptCache.invalidate(username);
    }
    public void addUserToLoginAttemptCache(String username)  {
        int attempts = 0;
        try {
            attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        loginAttemptCache.put(username,attempts);

    }

    public boolean hasExceededMaxAttempts(String username)  {
        try {
            return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
