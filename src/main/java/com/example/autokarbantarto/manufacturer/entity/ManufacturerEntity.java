package com.example.autokarbantarto.manufacturer.entity;

import com.example.autokarbantarto.core.CoreEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "manufacturer")
@Entity
public class ManufacturerEntity extends CoreEntity {

    @Column(name = "name",nullable = false,unique = true)
    private String name;

    public ManufacturerEntity(){

    }

    public ManufacturerEntity(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
