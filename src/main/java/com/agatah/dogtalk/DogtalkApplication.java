package com.agatah.dogtalk;

import com.agatah.dogtalk.model.*;
import com.agatah.dogtalk.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class DogtalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogtalkApplication.class, args);
    }



//    @Bean
//    CommandLineRunner init(LocationRepository locationRepository,
//                           RoleRepository roleRepository){
//        return args -> {
//
//            //LOCATIONS
//            locationRepository.save(new Location().setCity("Wrocław"));
//            locationRepository.save(new Location().setCity("Kraków"));
//            locationRepository.save(new Location().setCity("Gdańsk"));
//
//
//            //ROLES
//            roleRepository.save(new Role().setName("ROLE_PET_OWNER"));
//            roleRepository.save(new Role().setName("ROLE_BEHAVIORIST"));
//
//        };
//    }
}
