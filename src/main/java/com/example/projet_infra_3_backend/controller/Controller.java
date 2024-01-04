package com.example.projet_infra_3_backend.controller;

import com.example.projet_infra_3_backend.config.JWTTokenProvider;
import com.example.projet_infra_3_backend.exception.*;
import com.example.projet_infra_3_backend.modele.User;
import com.example.projet_infra_3_backend.modele.UserPrincipal;
import com.example.projet_infra_3_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.example.projet_infra_3_backend.constant.FilesConstant.*;
import static com.example.projet_infra_3_backend.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(value="/user")
public class Controller {
    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public Controller(UserService userService, AuthenticationManager authenticationManagerz, JWTTokenProvider jwtTokenProvider) {
        this.userService =userService;
        this.authenticationManager = authenticationManagerz;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/{username}/profile")
    public ResponseEntity<User> getProfile(@PathVariable("username") String username) throws UserNotFoundException {
        // Utilisez le service approprié pour récupérer les détails de l'utilisateur
        User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user)  {
        try {
            authenticate(user.getUsername(),user.getPassword());
            User loginUser = userService.findUserByUsername(user.getUsername());
            UserPrincipal userPrincipal = new UserPrincipal(loginUser);
            HttpHeaders jwtHeader = getJwtHeaders(userPrincipal);
            return new ResponseEntity<>(loginUser, jwtHeader, OK);
        }
        catch (LockedException e){
            throw new ResponseStatusException(LOCKED, "Suite à 5 tentatives incorrect, votre compte à été bloqué");
        }
        catch (BadCredentialsException e){
            throw new ResponseStatusException(BAD_REQUEST,"Pseudo ou mot de passe Incorrect, veuillez ressayez.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user)  {
        try {
            User newUser = userService.register(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail());
            return new ResponseEntity<>(newUser, OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(NOT_FOUND, "Utilisateur non trouvé");
        } catch (EmailExistException e) {
            throw new ResponseStatusException(CONFLICT,"L'email existe déjà");
        } catch (UsernameExistException e) {
            throw new ResponseStatusException(CONFLICT,"Le pseudo existe déjà");
        }
    }

    @PostMapping("/register/no-generated-password")
    public ResponseEntity<User> registerNoGeneratedPassword(@RequestBody User user)  {
        try {
            User newUser = userService.registerNoGeneratedPassword(user.getFirstName(),user.getPassword(),user.getLastName(),user.getUsername(),user.getEmail());
            return new ResponseEntity<>(newUser, OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(NOT_FOUND, "Utilisateur non trouvé");
        } catch (EmailExistException e) {
            throw new ResponseStatusException(CONFLICT,"L'email existe déjà");
        } catch (UsernameExistException e) {
            throw new ResponseStatusException(CONFLICT,"Le pseudo existe déjà");
        }

    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestParam ("firstName") String firstName,
                                           @RequestParam ("lastName") String lastName,
                                           @RequestParam ("username") String username,
                                           @RequestParam ("email") String email,
                                           @RequestParam ("role") String role,
                                           @RequestParam ("isActive") String isActive,
                                           @RequestParam ("isNonLocked") String isNonLocked,
                                           @RequestParam (value = "profileImage", required = false) MultipartFile profileImage) {

        try {
            User newUser = userService.addNewUser(firstName,lastName,username,email,role,Boolean.parseBoolean(isActive),Boolean.parseBoolean(isNonLocked), profileImage);
            return new ResponseEntity<>(newUser, OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(NOT_FOUND, "Utilisateur non trouvé");
        } catch (EmailExistException e) {
            throw new ResponseStatusException(CONFLICT,"L'email existe déjà");
        } catch (UsernameExistException e) {
            throw new ResponseStatusException(CONFLICT,"Utilisateur existe déjà");
        } catch (IOException | NotAnImageFileException e) {
            throw new ResponseStatusException(BAD_REQUEST,"Probleme avec l'upload de l'image");
        }

    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam ("currentUsername") String currentUsername,
                                           @RequestParam ("firstName") String firstName,
                                           @RequestParam ("lastName") String lastName,
                                           @RequestParam ("username") String username,
                                           @RequestParam ("email") String email,
                                           @RequestParam ("role") String role,
                                           @RequestParam ("isActive") String isActive,
                                           @RequestParam ("isNonLocked") String isNonLocked,
                                           @RequestParam (value = "profileImage", required = false) MultipartFile profileImage) {

        try {
            User updateUser = userService.updateUser(currentUsername,firstName,lastName,username,email,role,Boolean.parseBoolean(isActive),Boolean.parseBoolean(isNonLocked), profileImage);
            return new ResponseEntity<>(updateUser, OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(NOT_FOUND, "Utilisateur non trouvé");
        } catch (EmailExistException e) {
            throw new ResponseStatusException(CONFLICT,"L'email existe déjà");
        } catch (UsernameExistException e) {
            throw new ResponseStatusException(CONFLICT,"Utilisateur existe déjà");
        } catch (IOException | NotAnImageFileException e) {
            throw new ResponseStatusException(BAD_REQUEST,"Probleme avec l'upload de l'image");
        }

    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()){
            int bytesRead;
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) >0){
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }

        }
        return byteArrayOutputStream.toByteArray();
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users,OK);
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable("email") String email){
        try {
            userService.resetPassword(email);
            return new ResponseEntity<>(OK);
        } catch (EmailNotFoundException e) {
            throw new  ResponseStatusException(NOT_FOUND, email + " : cette email n'a pas été retrouvé");
        }
    }

    @DeleteMapping("/delete/{username}")
//    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username){
        try {
            userService.deleteUser(username);
            return new ResponseEntity<>("L'utilisateur a bien été supprimé", NO_CONTENT);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(NOT_FOUND, username + " : cette username n'a pas été retrouvé");
        } catch (IOException e) {
            throw new ResponseStatusException(BAD_REQUEST, "probleme avec la suppression du dossier d'image");
        }

    }

    private HttpHeaders getJwtHeaders(UserPrincipal user) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWT_TOKEN_HEADER,jwtTokenProvider.generateJwtToken(user));
        return httpHeaders;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }

}
