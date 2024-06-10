package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.auth.AuthenticationResponse;
import com.edu.hcmuaf.springserver.auth.RegisterAdminRequest;
import com.edu.hcmuaf.springserver.dto.request.UserRequest;
import com.edu.hcmuaf.springserver.dto.response.UserResponse;
import com.edu.hcmuaf.springserver.entity.User;
import com.edu.hcmuaf.springserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            username = ((OAuth2User) principal).getAttribute("email"); // assuming email is the username
        } else if (principal instanceof String) {
            username = principal.toString();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected principal type");
        }

        System.out.println("Username: " + username);

        User user = userService.getUserProfileByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone_number());
        userResponse.setFullName(user.getFull_name());
        userResponse.setGender(user.getGender());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        userResponse.setBirthday(sdf.format(user.getBirthday()));

        return ResponseEntity.ok(userResponse);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getListUser() {
        List<User> listUser = userService.getListUser();
        if (listUser != null) {
            return ResponseEntity.ok(listUser);
        } else return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User user = userService.findUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest.EditUser userRequest, Authentication authentication) throws ParseException {
        boolean update = userService.updateUser(userRequest);
        if(update) {
            return getProfile(authentication);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping ("/admin_create")
    public ResponseEntity<?> createUser(@RequestBody RegisterAdminRequest adminRequest) throws ParseException {
        AuthenticationResponse authenticationResponse = userService.createUser(adminRequest);
        if(authenticationResponse != null) {
            return ResponseEntity.ok(authenticationResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllShowTime(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "{}") String filter,
                                                     @RequestParam(defaultValue = "16") int perPage,
                                                     @RequestParam(defaultValue = "title") String sort,
                                                     @RequestParam(defaultValue = "DESC") String order) {
        Page<User> users = userService.getAllwithSort(filter, page, perPage, sort, order);
        return ResponseEntity.ok(users);
    }
}
