package com.example.projet_infra_3_backend.service;

import com.example.projet_infra_3_backend.config.Role;
import com.example.projet_infra_3_backend.exception.*;
import com.example.projet_infra_3_backend.modele.User;
import com.example.projet_infra_3_backend.modele.UserPrincipal;
import com.example.projet_infra_3_backend.repository.UserRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.example.projet_infra_3_backend.config.Role.ROLE_ADMIN;
import static com.example.projet_infra_3_backend.config.Role.ROLE_USER;
import static com.example.projet_infra_3_backend.constant.FilesConstant.*;
import static com.example.projet_infra_3_backend.constant.UserImplConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService,UserService{

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private LoginAttemptService loginAttemptService;

    private EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService, EmailService emailService){
        this.emailService = emailService;
        this.loginAttemptService = loginAttemptService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User user = add(firstName,lastName,username,email);
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        emailService.sendEmail(username, password, email);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User registerNoGeneratedPassword(String firstName, String password, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User user = add(firstName,lastName,username,email);
        user.setPassword(encodePassword(password));
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        userRepository.save(user);
        return user;
    }

    @Override
    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        User user = add(firstName,lastName,username,email);
        String password = generatePassword();
        user.setPassword(encodePassword(password));

        user.setNotLocked(isNonLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        saveProfileImage(user,profileImage);
        emailService.sendEmail(username, password, email);

        return user;
    }

    @Override
    public User updateUser(String currentUsername, String newFirstname, String newLastName, String newUsername, String newEemail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        User currentUser = validateNewUsernameAndEmail(currentUsername,newUsername,newEemail);
        currentUser.setFirstName(newFirstname);
        currentUser.setLastName(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEemail);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        saveProfileImage(currentUser,profileImage);
        return currentUser;
    }

    @Override
    public void deleteUser(String username) throws UserNotFoundException, IOException {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        // cela permet de bien supprimé le dossier qui a été créer pour stocker la photo de profile , quand on supprime
        Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
        FileUtils.deleteDirectory(new File(userFolder.toString()));
        userRepository.deleteById(user.getId());
    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        emailService.sendEmail(user.getUsername(),password,user.getEmail());
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        User user = validateNewUsernameAndEmail(username,null,null);
        saveProfileImage(user,profileImage);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            return userPrincipal;
        }
    }

    public User add(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());

        return user;
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        // on update ou cree un new ?
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null)
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);

            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId()))
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);

            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId()))
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            return currentUser;
        } else {
            if (userByNewUsername != null)
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            if (userByNewEmail != null)
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
        return null;
    }


    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }
    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null){
            if (!Arrays.asList(IMAGE_JPEG_VALUE,IMAGE_PNG_VALUE,IMAGE_GIF_VALUE).contains(profileImage.getContentType()))
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + "n'est pas une image. Veuillez choisir le bon type");
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)){
                Files.createDirectories(userFolder );
                LOGGER.info(DIRECTORY_CREATED);
            }
            Files.deleteIfExists(Paths.get(userFolder+user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION) , REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
    }
    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    public User addNewAdmin() throws UserNotFoundException, EmailExistException, UsernameExistException {
        User user = add("adminFirstName","adminLastName","admin","clement_guyot@outlook.fr");
        String password = "admin";
        user.setPassword(encodePassword(password));
        user.setNotLocked(true);
        user.setRole(ROLE_ADMIN.name());
        user.setAuthorities(ROLE_ADMIN.getAuthorities());
        userRepository.save(user);
        return user;
    }
    public void deleteAdmin() {
        User admin = userRepository.findUserByUsername("admin");
        if (admin != null)
            userRepository.delete(admin);
    }
}
