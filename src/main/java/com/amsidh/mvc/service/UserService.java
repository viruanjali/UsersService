package com.amsidh.mvc.service;

import com.amsidh.mvc.service.model.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto getUser(String userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(String userId, UserDto userDto);

    void deleteUser(String userId);

    List<UserDto> getAllUsers();

    UserDto getUserByEmailId(String emailId);

}
