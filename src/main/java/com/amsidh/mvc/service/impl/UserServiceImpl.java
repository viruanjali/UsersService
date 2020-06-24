package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.repository.AlbumServiceClient;
import com.amsidh.mvc.repository.UserRepository;
import com.amsidh.mvc.repository.entity.UserEntity;
import com.amsidh.mvc.service.UserService;
import com.amsidh.mvc.service.model.UserDto;
import com.amsidh.mvc.ui.exception.DuplicateUserException;
import com.amsidh.mvc.ui.exception.NoDataFoundException;
import com.amsidh.mvc.ui.exception.UserNotFoundException;
import com.amsidh.mvc.ui.model.AlbumResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {


    private final Environment environment;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    //private final RestTemplate restTemplate;
    private final AlbumServiceClient albumServiceClient;


    @Autowired
    public UserServiceImpl(Environment environment, UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, /*RestTemplate restTemplate*/ AlbumServiceClient albumServiceClient) {
        this.environment = environment;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        //this.restTemplate = restTemplate;
        this.albumServiceClient = albumServiceClient;
    }

    @Override
    public UserDto getUser(String userId) {
        log.info(format("getUser of class UserServiceImpl with userId %s", userId));
        Optional<UserEntity> userEntity = ofNullable(userRepository.findByUserId(userId)).orElseThrow(() -> new UserNotFoundException(userId));
        UserDto userDto = modelMapper.map(userEntity.get(), UserDto.class);
        List<AlbumResponseModel> albumResponseModels = getAlbumResponseModel(userId);
        log.info("Response received from albums-ws micro service");
        userDto.setAlbums(albumResponseModels);
        return userDto;
    }


    @Override
    public List<UserDto> getAllUsers() {
        log.info("getAllUsers of class UserServiceImpl called");
        List<UserEntity> entities = userRepository.findAll();
        if (entities.isEmpty()) {
            throw new NoDataFoundException();
        }
        Type useDtosType = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(entities, useDtosType);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("createUser of class UserServiceImpl");
        Optional<UserEntity> duplicationUserEntity = userRepository.findByEmailId(userDto.getEmailId());
        if (duplicationUserEntity.isPresent()) {
            throw new DuplicateUserException("EmailId", userDto.getEmailId());
        }
        ;
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(randomUUID().toString());
        UserEntity savedEntity = userRepository.save(userEntity);
        return modelMapper.map(savedEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        log.info("updateUser of class UserServiceImpl");
        UserEntity updateUserEntity = ofNullable(userRepository.findByUserId(userId).get()).map(userEntity -> {
            userEntity.setEmailId(userDto.getEmailId());
            userEntity.setFirstName(userDto.getFirstName());
            userEntity.setLastName(userDto.getLastName());
            return userEntity;
        }).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.flush();
        return modelMapper.map(updateUserEntity, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        log.info("deleteUser of class UserServiceImpl called");
        ofNullable(userRepository.findByUserId(userId).get())
                .map(Optional::of)
                .orElseThrow(() -> new UserNotFoundException(userId))
                .ifPresent(userEntity -> userRepository.delete(userEntity));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername of class UserServiceImpl called");
        UserEntity userEntity = ofNullable(userRepository.findByEmailId(username)).orElseThrow(() -> new UserNotFoundException("EmailId", username)).get();
        return new User(userEntity.getEmailId(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }


    @Override
    public UserDto getUserByEmailId(String emailId) {
        log.info("getUserByEmailId of class UserServiceImpl called");
        UserEntity userEntity = ofNullable(userRepository.findByEmailId(emailId)).orElseThrow(() -> new UserNotFoundException("EmailId", emailId)).get();
        return modelMapper.map(userEntity, UserDto.class);
    }

    List<AlbumResponseModel> getAlbumResponseModel(String userId) {
        log.info(format("Calling albums-ws service to get album list associated to userId %s", userId));
        return albumServiceClient.getAlbumsByUserId(userId);
    }
}
