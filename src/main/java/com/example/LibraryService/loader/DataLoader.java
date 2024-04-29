package com.example.LibraryService.loader;

import com.example.LibraryService.entity.Role;
import com.example.LibraryService.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String init;

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {


        try {
            if (init.equalsIgnoreCase("create")) {
                Role roleUser = new Role();
                roleUser.setId(1L);
                roleUser.setName("ROLE_USER");

                List<Role> roleList = new ArrayList<>(List.of(roleUser));
                roleRepository.saveAll(roleList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
