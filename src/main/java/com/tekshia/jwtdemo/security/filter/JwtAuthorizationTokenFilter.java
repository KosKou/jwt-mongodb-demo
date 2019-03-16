package com.tekshia.jwtdemo.security.filter;

import com.tekshia.jwtdemo.security.service.JwtService;
import com.tekshia.jwtdemo.security.service.JwtServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationTokenFilter extends BasicAuthenticationFilter {

    //Vars
    private JwtService jwtService;
    //Constructor
    public JwtAuthorizationTokenFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }


    //Reimplementamos el metodo para cambiar el "Basic " por "Bearer "
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(JwtServiceImpl.HEADER_STRING); //TODO: constant pending

        if (!requiresAuthentication(header)) {  //If this is false
            chain.doFilter(request, response);
            return;
        }



        UsernamePasswordAuthenticationToken authenticationToken = null;

        if (jwtService.validate(header)){

            authenticationToken = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header),
                    null, jwtService.getRoles(header));
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); //Maneja el contexto de seguridad
        chain.doFilter(request,response);

    }

    protected boolean requiresAuthentication(String header){

        if (header == null || !header.startsWith(JwtServiceImpl.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}
