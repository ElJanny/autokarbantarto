package com.example.autokarbantarto.user.entity;

import com.example.autokarbantarto.core.CoreEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Table(name = "app_role")
@Entity
public class RoleEntity extends CoreEntity implements GrantedAuthority {
    @Column(name = "authority")
    private String authority;

    @ManyToMany
    private List<UserEntity> Userd;

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}

