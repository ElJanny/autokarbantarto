package com.example.autokarbantarto.user.service.Impl;

import com.example.autokarbantarto.core.CoreCRUDServiceImpl;
import com.example.autokarbantarto.user.entity.RoleEntity;
import com.example.autokarbantarto.user.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends CoreCRUDServiceImpl<RoleEntity> implements RoleService {

    @Override
    protected void updateCore(RoleEntity persistedEntity, RoleEntity entity) {
        persistedEntity.setAuthority(entity.getAuthority());
    }

    @Override
    protected Class<RoleEntity> getManagedClass() {
        return RoleEntity.class;
    }
}
