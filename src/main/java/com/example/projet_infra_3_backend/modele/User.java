package com.example.projet_infra_3_backend.modele;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection ="users")
public class User {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  //pour le cacher
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String role; //ROLE_USER{read, edit}, ROLE_ADMIN {delete}
    private String[] authorities;
    private boolean isNotLocked;
}
