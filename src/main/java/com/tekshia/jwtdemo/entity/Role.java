package com.tekshia.jwtdemo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "role")
public class Role {

    @Id
    private String id;
    private String role;

    @DBRef
    private List<User> users;
    //    Constructor
    public Role() {
    }
    public Role(String role) {
        this.role = role;
    }

    //    Methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
