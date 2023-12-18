package com.example.projet_infra_3_backend.repository;

import com.example.projet_infra_3_backend.modele.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
