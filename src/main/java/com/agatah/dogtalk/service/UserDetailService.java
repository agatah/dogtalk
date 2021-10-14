package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.mappers.UserMapper;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findUserByEmail(email);
        if(userOpt.isPresent()){
            return UserMapper.toUserDetailsDto(userOpt.get());
        }
        throw new UsernameNotFoundException("User with email: " + email + "not found");
    }
}
