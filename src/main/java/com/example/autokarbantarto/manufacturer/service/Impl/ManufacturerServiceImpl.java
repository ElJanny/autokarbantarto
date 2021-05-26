package com.example.autokarbantarto.manufacturer.service.Impl;



import com.example.autokarbantarto.core.CoreCRUDServiceImpl;
import com.example.autokarbantarto.manufacturer.entity.ManufacturerEntity;
import com.example.autokarbantarto.manufacturer.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl extends CoreCRUDServiceImpl<ManufacturerEntity> implements ManufacturerService {

    @Override
    protected void updateCore(ManufacturerEntity persistedEntity, ManufacturerEntity entity) {
        persistedEntity.setName(entity.getName());
    }

    @Override
    protected Class<ManufacturerEntity> getManagedClass() {
        return ManufacturerEntity.class;
    }

        @Override
     public List<ManufacturerEntity> getAll(String name){
            return entityManager.createQuery("SELECT n FROM " + getManagedClass().getSimpleName() + " n WHERE n.name like '"+name+"%'", getManagedClass()).getResultList();
        }

}
