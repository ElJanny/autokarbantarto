package com.example.autokarbantarto.manufacturer.service;


import com.example.autokarbantarto.core.CoreCRUDService;
import com.example.autokarbantarto.manufacturer.entity.ManufacturerEntity;

import java.util.List;

public interface ManufacturerService extends CoreCRUDService<ManufacturerEntity> {

      List<ManufacturerEntity> getAll(String name);
}
