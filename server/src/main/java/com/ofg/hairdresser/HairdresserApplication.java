package com.ofg.hairdresser;

import com.ofg.hairdresser.model.entity.Role;
import com.ofg.hairdresser.model.entity.User;
import com.ofg.hairdresser.repository.RoleRepository;
import com.ofg.hairdresser.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class HairdresserApplication {
    public static void main(String[] args) {
        SpringApplication.run(HairdresserApplication.class, args);
    }

    @Bean
    public CommandLineRunner createUsers(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_USER");
                        return roleRepository.save(role);
                    });

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_ADMIN");
                        return roleRepository.save(role);
                    });

            Role hairdresserRole = roleRepository.findByName("ROLE_HAIRDRESSER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_HAIRDRESSER");
                        return roleRepository.save(role);
                    });

            if (userRepository.findByEmail("omer@gmail.com").isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(userRole);

                User user = new User();
                user.setEmail("omer@gmail.com");
                user.setPassword(passwordEncoder.encode("P4ssword"));
                user.setFirstName("omer");
                user.setLastName("gulhan");
                user.setProfileImage("default.png");
                user.setActive(true);
                user.setRoles(roles);
                userRepository.save(user);
            }

            if (userRepository.findByEmail("faruk@gmail.com").isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(userRole);

                User user = new User();
                user.setEmail("faruk@gmail.com");
                user.setPassword(passwordEncoder.encode("P4ssword"));
                user.setFirstName("faruk");
                user.setLastName("gulhan");
                user.setProfileImage("default.png");
                user.setActive(true);
                user.setRoles(roles);
                userRepository.save(user);
            }

            if (userRepository.findByEmail("hairdresser@gmail.com").isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(hairdresserRole);
                roles.add(userRole);

                User user = new User();
                user.setEmail("hairdresser@gmail.com");
                user.setPassword(passwordEncoder.encode("P4ssword"));
                user.setFirstName("hairdresser");
                user.setLastName("test");
                user.setProfileImage("default.png");
                user.setActive(true);
                user.setRoles(roles);
                userRepository.save(user);
            }
        };
    }

}