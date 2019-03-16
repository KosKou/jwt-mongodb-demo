package com.tekshia.jwtdemo.configuration;

import com.tekshia.jwtdemo.security.filter.JwtAuthenticationTokenFilter;
import com.tekshia.jwtdemo.security.filter.JwtAuthorizationTokenFilter;
import com.tekshia.jwtdemo.security.service.JwtService;
import com.tekshia.jwtdemo.security.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
    @CompanyName:   TekShia.Inc
    @Author:        KosKou
    @GitHub:        github.com/koskou
    @CreationDate:  06/03/2019
    @Description:   Security Configuration
*/
//
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired          //Inject the class where this interface is implemented and have @Component
    private JwtService jwtService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/","/css/**","/js/**","/fill").permitAll()
                .antMatchers("/listar").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationTokenFilter(authenticationManager(), jwtService))
                .addFilter(new JwtAuthorizationTokenFilter(authenticationManager(), jwtService));

        //We are not going to use CSRF forms so, disable to work with JWT(STATELESS)
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //->JWT
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
