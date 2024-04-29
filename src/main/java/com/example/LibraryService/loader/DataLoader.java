package com.example.LibraryService.loader;

import com.example.LibraryService.entity.Admin;
import com.example.LibraryService.entity.Parent;
import com.example.LibraryService.entity.Role;
import com.example.LibraryService.repository.AdminRepository;
import com.example.LibraryService.repository.ParentRepository;
import com.example.LibraryService.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String init;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final ParentRepository parentRepository;

    private final AdminRepository adminRepository;

    @Override
    public void run(String... args) {


        try {
            if (init.equalsIgnoreCase("create")) {
                Role roleAdmin = new Role();
                roleAdmin.setId(1L);
                roleAdmin.setName("ROLE_ADMIN");


                Role roleUser = new Role();
                roleUser.setId(2L);
                roleUser.setName("ROLE_USER");

                List<Role> roleList = new ArrayList<>(List.of(roleAdmin,roleUser));
                roleRepository.saveAll(roleList);

                Parent parent=new Parent();
                parent.setEmail("abror.developer@gmail.com");
                parent.setUserName("abror123");
                parent.setPassword(passwordEncoder.encode("123"));
                parent.setRoles(roleList);

                parentRepository.save(parent);

                Admin admin=new Admin();
                admin.setFullName("Abror Allaberganov");
                admin.setParent(parent);

                adminRepository.save(admin);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
