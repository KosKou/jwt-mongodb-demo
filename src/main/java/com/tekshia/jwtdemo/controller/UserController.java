package com.tekshia.jwtdemo.controller;

import com.tekshia.jwtdemo.entity.Role;
import com.tekshia.jwtdemo.entity.User;
import com.tekshia.jwtdemo.service.RoleService;
import com.tekshia.jwtdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //    Initial Roles set
    @GetMapping("/fillUsers")
    public String fill(){
        Optional<User> admin = userService.findByEmail("admin@gmail.com");
        if (!admin.isPresent()){
            userService.createUser(new User("admin@gmail.com","123456"),"ADMIN");
        }
        return "The users have been initialized";
    }

    @GetMapping("/users")
    public List<User> listAll(){
        return userService.findAll();
    }
}
