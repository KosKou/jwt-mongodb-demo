package com.tekshia.jwtdemo.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
    @CompanyName:   TekShia.Inc
    @Author:        KosKou
    @GitHub:        github.com/koskou
    @CreationDate:  00/00/0000
    @Description:   A class used to convert an array to string
*/
public abstract class SimpleGrantedAuthorityMixin {

    @JsonCreator
    public SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {

    }

}
