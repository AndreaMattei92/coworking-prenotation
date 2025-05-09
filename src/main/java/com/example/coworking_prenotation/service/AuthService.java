package com.example.coworking_prenotation.service;

import com.example.coworking_prenotation.dto.userdto.UserRegistrationDTO;
import com.example.coworking_prenotation.entity.Role;
import com.example.coworking_prenotation.entity.User;
import com.example.coworking_prenotation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Value("${spring.keycloak.auth-server-url}")
    private String keycloakBaseUrl;

    @Value("${spring.keycloak.realm}")
    private String keycloakRealm;

    @Value("${spring.keycloak.resource}")
    private String keycloakClientId;

    @Value("${spring.keycloak.credentials.secret}")
    private String keycloakClientSecret;

    private final RestTemplate restTemplate= new RestTemplate();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(UserRegistrationDTO userRegistrationDTO) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isEmpty()) {

            Map<String, Object> user = new HashMap<>();
                user.put("username", userRegistrationDTO.getEmail());
                user.put("enabled", true);
                user.put("email", userRegistrationDTO.getEmail());

            Map<String, Object> credential = new HashMap<>();
                credential.put("type", "password");
                credential.put("value", userRegistrationDTO.getPassword());
                credential.put("temporary", false);

            user.put("credentials", List.of(credential));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);
            String createUserUrl = keycloakBaseUrl + "/admin/realms/" + keycloakRealm + "/users";

            restTemplate.postForEntity(createUserUrl, request, String.class);


           User newuser = new User();
            newuser.setName(userRegistrationDTO.getName());
            newuser.setEmail(userRegistrationDTO.getEmail());
            newuser.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
            newuser.setRole(Role.USER);
            userRepository.save(newuser);

            return "User registered successfully";
        } else {
            return "Email already in use";
        }
    }

    private String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", keycloakClientId);
        body.add("client_secret", keycloakClientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        String tokenUrl = keycloakBaseUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    public String login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", keycloakClientId);
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        String tokenUrl = keycloakBaseUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getBody() != null && response.getBody().get("access_token") != null) {
            return response.getBody().get("access_token").toString();
        } else {
            throw new IllegalArgumentException("Access token not found in response");
        }
    }

//    public String login(String username, String password) {
//
//        String keycloakUrl = keycloakBaseUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";
//
//        RestTemplate restTemplate = new RestTemplate();
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("grant_type", "password");
//        requestBody.put("client_id", keycloakClientId);
//        requestBody.put("username", username);
//        requestBody.put("password", password);
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(keycloakUrl, requestBody, Map.class);
//
//        if (response.getBody() != null && response.getBody().get("access_token") != null) {
//            return response.getBody().get("access_token").toString();
//        } else {
//            throw new IllegalArgumentException("Access token not found in response");
//        }
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUserById(long id) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Optional<User> user = userRepository.findById(id);

        String searchUserUrl = keycloakBaseUrl + "/admin/realms/" + keycloakRealm + "/users?email=" + user.get().getEmail();

        ResponseEntity<User[]> response = restTemplate.exchange(
                searchUserUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User[].class
        );
        User[] users = response.getBody();
        if (users == null || users.length == 0) {
            throw new RuntimeException("Utente con email " + user.get().getEmail() + " non trovato in Keycloak.");
        }

        String userId = String.valueOf(users[0].getId()); // puoi prendere solo il primo risultato

        // 2. Elimina l'utente usando il suo ID
        String deleteUserUrl = keycloakBaseUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId;

        restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        // 3. Elimina l'utente dal database locale
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } else {
            throw new IllegalArgumentException("User not found");
        }
        }
}
