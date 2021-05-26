package com.example.autokarbantarto.user.service;

import com.example.autokarbantarto.core.CoreCRUDService;
import com.example.autokarbantarto.user.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends CoreCRUDService<UserEntity>, UserDetailsService {

    List<UserEntity> getAll(String userName,String firstName,String lastName);
}
