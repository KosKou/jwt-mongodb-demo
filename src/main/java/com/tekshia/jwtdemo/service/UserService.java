package com.tekshia.jwtdemo.service;

import com.tekshia.jwtdemo.entity.Role;
import com.tekshia.jwtdemo.entity.User;
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

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<User> findAllByName(String firstName){
        return userRepository.findAllByFirstName(firstName);
    }

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean isUserExist(String id){
        return userRepository.existsById(id);
    }


    public User createUser(User user, Role role){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        //Auth Authority
        Role userRole = role;

        if (user.getRoles() == null || user.getRoles().isEmpty()){
            List<Role> roles = new ArrayList<>();
            roles.add(userRole);
            user.setRoles(roles);
        }else{
            List<Role> roles = user.getRoles();
            roles.add(userRole);
            user.setRoles(roles);
        }

        return userRepository.save(user);

    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
}
