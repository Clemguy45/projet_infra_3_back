package com.example.projet_infra_3_backend;

import com.example.projet_infra_3_backend.modele.User;
import com.example.projet_infra_3_backend.service.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ProjetInfra3BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetInfra3BackendApplication.class, args);
    }


    // pour que le cors nous laisse récuperer notre jwt token
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow all origins, methods, and headers. You should adjust this based on your security requirements.
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner start(UserServiceImpl userService){
        return args -> {
            userService.deleteAdmin();
            User admin = userService.addNewAdmin();

            List<User> listUser = new ArrayList<>();
            listUser.add(admin);
        };
    }
}
