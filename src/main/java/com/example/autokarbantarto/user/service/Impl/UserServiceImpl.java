package com.example.autokarbantarto.user.service.Impl;

import com.example.autokarbantarto.core.CoreCRUDServiceImpl;
import com.example.autokarbantarto.user.entity.UserEntity;
import com.example.autokarbantarto.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class UserServiceImpl extends CoreCRUDServiceImpl<UserEntity> implements UserService {
    @Override
    protected void updateCore(UserEntity persistedEntity, UserEntity entity) {
        persistedEntity.setAuthorities(entity.getAuthorities());
        persistedEntity.setUsername(entity.getUsername());
    }

    @Override
    protected Class<UserEntity> getManagedClass() {
        return UserEntity.class;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TypedQuery<UserEntity> query = entityManager.createNamedQuery(UserEntity.FIND_USER_BY_USERNAME, UserEntity.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    @Override
    public List<UserEntity> getAll(String userName,String firstName,String lastName){
        return entityManager.createQuery("SELECT n FROM " + getManagedClass().getSimpleName() + " n WHERE n.firstName like '"+firstName+"%'"+" AND n.lastName like '"+lastName+"%' AND n.username like '"+userName+"%'", getManagedClass()).getResultList();
    }
}
