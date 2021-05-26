package com.example.autokarbantarto.car.service.Impl;


import com.example.autokarbantarto.car.entity.CarEntity;
import com.example.autokarbantarto.car.service.CarService;
import com.example.autokarbantarto.core.CoreCRUDServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl extends CoreCRUDServiceImpl<CarEntity> implements CarService {

    @Override
    protected void updateCore(CarEntity persistedEntity, CarEntity entity) {
        persistedEntity.setManufacturer(entity.getManufacturer());
        persistedEntity.setType(entity.getType());
        persistedEntity.setNumberOfDoors(entity.getNumberOfDoors());
        persistedEntity.setYear(entity.getYear());
    }

    @Override
    protected Class<CarEntity> getManagedClass() {
        return CarEntity.class;
    }

    @Override
    public List<CarEntity> getAll(String type, Integer nod, Integer year) {
        String query = "";
        if(nod!=null)
        {
            query+=" AND n.numberOfDoors = " + nod;
        }
        if(year!=null)
        {
            query+= " AND n.year = "+ year;
        }
        return entityManager.createQuery("SELECT n FROM " + getManagedClass().getSimpleName() + " n WHERE n.type like '"+type+"%'"+query, getManagedClass()).getResultList();
    }

}
