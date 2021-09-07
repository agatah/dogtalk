package com.agatah.dogtalk;

import com.agatah.dogtalk.model.*;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.model.enums.RoleType;
import com.agatah.dogtalk.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DogtalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogtalkApplication.class, args);
    }

    @Bean
    CommandLineRunner init(LocationRepository locationRepository, RoleRepository roleRepository,
                           PrivilegeRepository privilegeRepository){
        return args -> {

            //LOCATIONS
            if(locationRepository.count() == 0){
                locationRepository.save(new Location().setCity("Wrocław"));
                locationRepository.save(new Location().setCity("Kraków"));
                locationRepository.save(new Location().setCity("Gdańsk"));
            }

            //ROLES
            if(roleRepository.count() == 0){
                roleRepository.save(new Role().setRoleType(RoleType.ROLE_BEHAVIORIST));
                roleRepository.save(new Role().setRoleType(RoleType.ROLE_PET_OWNER));
            }

            //PRIVILEGES
            if(privilegeRepository.count() == 0){
                privilegeRepository.save(new Privilege().setPrivilegeType(PrivilegeType.MANAGE));
                privilegeRepository.save(new Privilege().setPrivilegeType(PrivilegeType.EDIT));
                privilegeRepository.save(new Privilege().setPrivilegeType(PrivilegeType.RESPOND));
                privilegeRepository.save(new Privilege().setPrivilegeType(PrivilegeType.JOIN_REQUEST));
            }

        };
    }
}
