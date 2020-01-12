package com.tekshia.jwtdemo.service;

import com.tekshia.jwtdemo.entity.Role;
import com.tekshia.jwtdemo.entity.User;
import com.tekshia.jwtdemo.repository.RoleRepository;
import com.tekshia.jwtdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public boolean isUserExist(String id){
        return userRepository.existsById(id);
    }

    public User createUser(User user, String auth){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setStatus("ACTIVE");
        user.setEnabled(true);
//        Validate Role
        Optional<Role> role = roleRepository.findByRole(auth);
        if (role.isPresent()){
            //If user does not have any role
            if (user.getRoles() == null || user.getRoles().isEmpty()){
                List<Role> roles = new ArrayList<>();
                roles.add(role.get());
                user.setRoles(roles);
            }else{
//              If user does have some role
                List<Role> roles = user.getRoles();
                //Validate not same role
                for (Role r : roles) {
                    if (r.getRole().equals(role)){
                        return null;
                    }
                }
                roles.add(role.get());
                user.setRoles(roles);
            }
        }else {
            return null;
        }
        return userRepository.save(user);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(String id){
        Optional<User> user = userRepository.findById(id);

        user.get().setStatus("DELETED");
        updateUser(user.get());
    }
}
