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

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/addUsers")
    public String someUser(){
        userService.createUser(new User("user@gmail.com","123456"), roleService.findByName("USER"));
        userService.createUser(new User("admin@gmail.com","123456"), roleService.findByName("ADMIN"));
        return "Usuarios Creados";
    }


    @GetMapping("/fillRoles")
    public String fill(){
        if (roleService.findByName("ADMIN") == null){
            roleService.createRole(new Role("ADMIN"));
        }

        if (roleService.findByName("USER") == null){
            roleService.createRole(new Role("USER"));
        }
        roleService.createRole(new Role("TEST"));
        return "Thats it";
    }

    @GetMapping("/listar")
    public List<Role> listar(){
        return roleService.findAll();
    }


    @GetMapping("/addRole")
    public String addRole(Principal principal){
        String email = principal.getName();
        User user = userService.findByEmail(email);
        List<Role> roles = user.getRoles();
        roles.add(roleService.findByName("ADMIN"));
        user.setRoles(roles);

        userService.updateUser(user);
        return "Roles are updated";
    }
}
