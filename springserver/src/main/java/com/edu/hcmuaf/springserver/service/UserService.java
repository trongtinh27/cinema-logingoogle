package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.auth.*;
import com.edu.hcmuaf.springserver.config.VNPayConfig;
import com.edu.hcmuaf.springserver.dto.request.UserRequest;
import com.edu.hcmuaf.springserver.entity.User;
import com.edu.hcmuaf.springserver.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailService emailService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    public List<User> getListUser() {
        return userRepository.findAll();
    }

    public User getUserProfileByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {
        return authenticateAndGenerateToken(authenticationRequest, false);
    }

    public AuthenticationResponse adminAuthentication(AuthenticationRequest authenticationRequest) {
        return authenticateAndGenerateToken(authenticationRequest, true);
    }

    public AuthenticationResponse authenticationGoogle(User user) {
        return authenticateAndGenerateToken(user);
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsUserByUsername(registerRequest.getUsername()) || userRepository.existsUserByEmail(registerRequest.getEmail())) {
            return AuthenticationResponse.builder().code(400).message("Username or email already exists").build();
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(encoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPhone_number("");
        newUser.setFull_name(registerRequest.getUsername().isEmpty() ? registerRequest.getEmail() : registerRequest.getFullname());
        newUser.setRole("user");
        newUser.setGender("Nam");
        newUser.setBirthday(Date.valueOf("2002-01-01"));

        userRepository.save(newUser);

        return authenticateAndGenerateToken(new AuthenticationRequest(newUser.getUsername(), registerRequest.getPassword()), false);
    }

    private AuthenticationResponse authenticateAndGenerateToken(AuthenticationRequest authenticationRequest, boolean isAdmin) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            User user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            System.out.println("user1: " + user);

            if (isAdmin && !user.getRole().equals("admin")) {
                return AuthenticationResponse.builder().code(401).message("Not an admin").build();
            }

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
            String jwtToken = jwtService.generateToken(user, authorities);

            return AuthenticationResponse.builder().code(200).message("Succeed").token(jwtToken).tokenExpirationTime(jwtService.getTokenExpirationTime()).build();
        } catch (AuthenticationException e) {
            return AuthenticationResponse.builder().code(401).message("User not found").build();
        }
    }

    private AuthenticationResponse authenticateAndGenerateToken(User user) {
        try {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
            String jwtToken = jwtService.generateToken(user, authorities);

            return AuthenticationResponse.builder().code(200).message("Succeed").token(jwtToken).tokenExpirationTime(jwtService.getTokenExpirationTime()).build();
        } catch (AuthenticationException e) {
            return AuthenticationResponse.builder().code(401).message("User not found").build();
        }
    }

    public boolean updateUser(UserRequest.EditUser userRequest) throws ParseException {

        User user = userRepository.findByUsername(userRequest.getUsername()).orElse(null);

        if(user!=null) {
            if(userRequest.isChangePassword()) {
                user.setPassword(encoder.encode(userRequest.getPassword()));
            }
            user.setEmail(userRequest.getEmail());
            user.setPhone_number(userRequest.getPhone());
            user.setFull_name(userRequest.getFullName());
            user.setGender(userRequest.getGender());

            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date parsedDate = inputFormat.parse(userRequest.getBirthday());
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            user.setBirthday(sqlDate);

            userRepository.save(user);

            return true;
        }
        return false;
    }

    public AuthenticationResponse createUser(RegisterAdminRequest adminRequest) {
        if (userRepository.existsUserByUsername(adminRequest.getUsername()) || userRepository.existsUserByEmail(adminRequest.getEmail())) {
            return AuthenticationResponse.builder().code(400).message("Username or email already exists").build();
        }
        User user = new User();
        user.setEmail(adminRequest.getEmail());
        user.setUsername(adminRequest.getUsername());
        user.setBirthday(adminRequest.getBirthday());
        user.setGender(adminRequest.getGender());
        user.setRole(adminRequest.getRole());
        user.setFull_name(adminRequest.getFull_name());
        user.setPassword(encoder.encode(adminRequest.getPassword()));
        user.setPhone_number(adminRequest.getPhone_number());
        userRepository.save(user);
        return AuthenticationResponse.builder().code(200).message("Create admin success").build();
    }

    public User findUserById(int id) {
        return userRepository.findUsersById(id);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public void deleteById(long id) { userRepository.deleteById(id);}

    public Page<User> getAllwithSort(String filter, int page, int perPage, String sortBy, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("q")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("username"), "%" + filterJson.get("q").asText().toLowerCase() + "%"));
            }
            if (filterJson.has("username")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("username"), "%" + filterJson.get("username").asText() + "%"));
            }
            if (filterJson.has("email")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("email"), "%" + filterJson.get("email").asText() + "%"));
            }
            return predicate;
        };
        if (sortBy.equals("username")) {
            return userRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "username")));
        }
        if (sortBy.equals("email"))  {
            return userRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "email")));
        }
        return userRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, sortBy)));
    }

    public AuthenticationResponse resetPassword(ResetPasswordRequest resetPasswordRequest) throws MessagingException {
        User user = userRepository.findByUsernameAndEmail(resetPasswordRequest.getUsername(), resetPasswordRequest.getEmail());
        if(user == null) {
            return AuthenticationResponse.builder().code(400).message("Username or email already exists").build();
        }
        String newPassword = VNPayConfig.getRandomNumber(8);
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        emailService.sendTextEmail(user.getEmail(),"Mật khẩu mới","Mật khẩu mới của tài khoản: " + user.getUsername() + " là: " + newPassword);

        return AuthenticationResponse.builder().code(200).message("Reset password success").build();
    }
}

