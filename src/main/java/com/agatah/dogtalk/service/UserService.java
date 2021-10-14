package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.dto.mappers.UserMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.model.Role;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.model.enums.RoleType;
import com.agatah.dogtalk.repository.RoleRepository;
import com.agatah.dogtalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BehavioristService behavioristService;
    private final OwnerService ownerService;
    private final PhotoService  photoService;
    private final PasswordEncoder passwordEncoder;

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

        Optional<Role> roleOpt = roleRepository.findByRoleType(userDto.getRole());
        if(roleOpt.isEmpty()){
            Role dbRole = roleRepository.save(new Role(userDto.getRole()));
            user.setRole(dbRole);
        } else {
            user.setRole(roleOpt.get());
        }

        User dbUser = userRepository.save(user);

        if(userDto.getRole().equals(RoleType.ROLE_BEHAVIORIST)){
            behavioristService.createBehavioristProfile(dbUser);

        } else if(userDto.getRole().equals(RoleType.ROLE_PET_OWNER)){
            ownerService.createOwnerProfile(dbUser);
        }

        return UserMapper.toUserDto(dbUser);
    }

    public UserDto findUserById(Long id){
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            return UserMapper.toUserDto(userOpt.get());
        } else {
            throw new EntityNotFoundException(User.class, id);
        }
    }

    public UserDto updateUser(Long id, UserDto userDto){
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            User dbUser = userOpt.get();
            dbUser.setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName());

            return UserMapper.toUserDto(userRepository.save(dbUser));
        }
        throw new EntityNotFoundException(User.class, id);
    }

    public void updatePassword(Long userId, UserDto userDto){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            User dbUser = userOpt.get();

            dbUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(dbUser);
        } else {
            throw new EntityNotFoundException(User.class, userId);
        }

    }

    public UserDto addPhoto(Long userId, Photo photo){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            User dbUser = userOpt.get();
            dbUser.setPhoto(photoService.uploadPhoto(photo));

            return UserMapper.toUserDto(userRepository.save(dbUser));
        } else {
            throw new EntityNotFoundException(User.class, userId);
        }
    }

    @Transactional
    public void deleteUserById(Long userId){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            if(userOpt.get().getRole().getRoleType().equals(RoleType.ROLE_BEHAVIORIST)){
                behavioristService.deleteById(userId);
            } else if (userOpt.get().getRole().getRoleType().equals(RoleType.ROLE_PET_OWNER)){
                ownerService.deleteById(userId);
            }
            userRepository.deleteById(userId);
        }
    }
}
