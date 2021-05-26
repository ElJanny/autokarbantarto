package com.example.autokarbantarto.user.config;

import com.example.autokarbantarto.user.entity.RoleEntity;
import com.example.autokarbantarto.user.entity.UserEntity;
import com.example.autokarbantarto.user.service.RoleService;
import com.example.autokarbantarto.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class UserAppInitConfig {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @PostConstruct
    private void init() {
        List<RoleEntity> roleEntities = roleService.getAll();
        RoleEntity admin = new RoleEntity();
        RoleEntity user = new RoleEntity();
        if (roleEntities.isEmpty()) {
            admin.setAuthority("ROLE_ADMIN");
            user.setAuthority("ROLE_USER");
            roleService.add(admin);
            roleService.add(user);
        }

        List<UserEntity> userEntities = userService.getAll();
        if (userEntities.isEmpty()) {
            UserEntity entity = new UserEntity();
            entity.setPassword(new BCryptPasswordEncoder().encode("almafa123"));
            entity.setLastName("admin");
            entity.setFirstName("admin");
            entity.setUsername("admin");
            entity.setAuthorities(new ArrayList<>());
            entity.getAuthorities().add(admin);
            userService.add(entity);

        }
    }
}

