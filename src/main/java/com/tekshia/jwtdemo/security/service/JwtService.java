package com.tekshia.jwtdemo.security.service;


/*
    @CompanyName:   TekShia.Inc
    @Author:        KosKou
    @GitHub:        github.com/koskou
    @CreationDate:  00/00/0000
    @Description:   General requirements to generate a JWT
*/

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public interface JwtService {
    public String create(Authentication auth) throws JsonProcessingException;
    public boolean validate(String token);
    public Claims getClaims(String token);      //Obtains claims from token
    public String getUsername(String token);    //Obtains username from token
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
    public String resolve(String token);        //Replace "Bearer " to ""
}
