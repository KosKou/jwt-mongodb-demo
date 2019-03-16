package com.tekshia.jwtdemo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekshia.jwtdemo.entity.User;
import com.tekshia.jwtdemo.security.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
    @CompanyName:   TekShia.Inc
    @Author:        KosKou
    @GitHub:        github.com/koskou
    @CreationDate:  13/03/2019
    @Description:   Esta clase se tiene que implementar en  como un filter
    UsernamePassAuth extends from OncePerRequestFilter so do not get mad
*/
public class JwtAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    //Vars
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    //El encargado de hacer el login segun el UserDetailsService    //Constructor
    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
//        String username = request.getParameter("username");   The Same than up
        String password = obtainPassword(request);

//        if (username == null) {
//            username = "";
//        }
//
//        if (password == null) {
//            password = "";
//        }

        if (username != null && password != null){
            logger.info("Username desde request paramenter (form-data): "+ username);
            logger.info("Password desde request paramenter (form-data): "+ password);
        }
        else {     //En el caso de RAW - json
            User user = null;
            try {
                user = new ObjectMapper().readValue(request.getInputStream(), User.class);

                username = user.getEmail();
                password = user.getPassword();

                logger.info("Username desde request InputStream (raw): "+ username);
                logger.info("Password desde request InputStream (raw): "+ password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        username = username.trim();

        //Token interno
        //Token distinto a JWT
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = jwtService.create(authResult);

        //Enviamos al usuarios | El Bearer es standard
        response.addHeader("Authorization", "Bearer " + token);

        //Podemos enviar lo que queramos
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("token", token);
        body.put("user", (org.springframework.security.core.userdetails.User) authResult.getPrincipal());
        body.put("mensaje", String.format("Hola %s, has iniciado sesion con exito!", authResult.getName()));

        //Convertimos el Map a JSON | Guardamos el body al response
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);        //OK
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("mensaje", "Error de autenticación: username o password incorrecto");
        body.put("error", failed.getMessage());

        //Convertimos el Map a JSON | Guardamos el body al response
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);        //403 - Acceso Prohibido | 401 - No autorizado
        response.setContentType("application/json");
    }
}
