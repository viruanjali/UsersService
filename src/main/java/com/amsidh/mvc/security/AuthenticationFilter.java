package com.amsidh.mvc.security;

import com.amsidh.mvc.service.UserService;
import com.amsidh.mvc.service.model.UserDto;
import com.amsidh.mvc.ui.model.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment environment;

    public AuthenticationFilter(UserService userService, Environment environment, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication of class AuthenticationFilter called");
        try {
            LoginRequestModel loginRequestModel = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestModel.getUserName(),
                            loginRequestModel.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("successfulAuthentication of class AuthenticationFilter called");
        User user = (User) authentication.getPrincipal();
        UserDto userDto = userService.getUserByEmailId(user.getUsername());

        String jwtToken = builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(currentTimeMillis() + parseLong(environment.getProperty("jwt.token.expirationTime"))))
                .signWith(HS512, environment.getProperty("jwt.token.secret"))
                .compact();

        response.setHeader("token", jwtToken);
        response.setHeader("userId", userDto.getUserId());


    }
}
