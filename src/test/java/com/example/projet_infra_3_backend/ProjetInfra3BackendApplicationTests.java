package com.example.projet_infra_3_backend;

import com.example.projet_infra_3_backend.config.JwtAuthorizationFilter;
import com.example.projet_infra_3_backend.exception.EmailExistException;
import com.example.projet_infra_3_backend.exception.UserNotFoundException;
import com.example.projet_infra_3_backend.exception.UsernameExistException;
import com.example.projet_infra_3_backend.modele.User;
import com.example.projet_infra_3_backend.service.UserService;
import com.example.projet_infra_3_backend.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.uri = mongodb+srv://projetinfra3user:projetinfra3password@projetinfra3.pslp1fj.mongodb.net/?retryWrites=true&w=majority"
})
class ProjetInfra3BackendApplicationTests {

}
