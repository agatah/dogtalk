package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.dto.mappers.UserMapper;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.model.Role;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.repository.RoleRepository;
import com.agatah.dogtalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BehavioristService behavioristService;
    private OwnerService ownerService;
    private PhotoService  photoService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       BehavioristService behavioristService,
                       OwnerService ownerService, PhotoService photoService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.behavioristService = behavioristService;
        this.ownerService = ownerService;
        this.photoService = photoService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isEmailAlreadyRegistered(UserDto user){
        return userRepository.existsUserByEmail(user.getEmail());
    }

    public UserDto createNewUser(UserDto userDto){
        User user = new User()
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setEmail(userDto.getEmail())
                .setPassword(passwordEncoder.encode(userDto.getPassword()));

        Optional<Role> roleOpt = roleRepository.findByName(userDto.getRole());
        if(roleOpt.isEmpty()){
            return null; //THROW ROLE NOT FOUND TODO
        }
        user.setRoles(Arrays.asList(roleOpt.get()));
        User dbUser = userRepository.save(user);

        if(roleOpt.get().getName().equals("ROLE_BEHAVIORIST")){
            behavioristService.createBehavioristProfile(dbUser);
        } else if(roleOpt.get().getName().equals("ROLE_PET_OWNER")){
            ownerService.createOwnerProfile(dbUser);
        }

        return UserMapper.toUserDto(dbUser);
    }

    public UserDto findUserById(Long id){
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            return UserMapper.toUserDto(userOpt.get());
        }
        return null;
    }

    public UserDto updateUser(Long id, UserDto user){
        User dbUser = userRepository.findById(id).get();

        dbUser.setFirstName(user.getFirstName())
                .setLastName(user.getLastName());

        userRepository.save(dbUser);

        return UserMapper.toUserDto(userRepository.findById(id).get());
    }

    public void updatePassword(Long userId, UserDto user){
        User dbUser = userRepository.findById(userId).get();
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(dbUser);
    }

    public UserDto addPhoto(Long userId, Photo photo){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            return UserMapper.toUserDto(userRepository.save(userOpt.get().setPhoto(photoService.uploadPhoto(photo))));
        }
        return null;
    }
}
