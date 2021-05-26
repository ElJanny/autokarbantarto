package com.example.autokarbantarto.car.service;

import com.example.autokarbantarto.car.entity.CarEntity;
import com.example.autokarbantarto.core.CoreCRUDService;

import java.util.List;


public interface CarService extends CoreCRUDService<CarEntity> {


    List<CarEntity> getAll(String type,Integer nod, Integer year);


}
