package com.example.autokarbantarto.car.entity;

import com.example.autokarbantarto.core.CoreEntity;
import com.example.autokarbantarto.manufacturer.entity.ManufacturerEntity;

import javax.persistence.*;



@Table(name = "car")
@Entity
public class CarEntity extends CoreEntity {

    @Column(name = "type",nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    private ManufacturerEntity manufacturer;

    @Column(name = "numberOfDoors",nullable = false)
    private Integer numberOfDoors;

    @Column(name = "year",nullable = false)
    private Integer year;

     public CarEntity(){

     }
    public CarEntity(String type, Integer numberOfDoors, ManufacturerEntity manufacturer, Integer year){
        this.type = type;
        this.numberOfDoors = numberOfDoors;
        this.manufacturer = manufacturer;
        this.year = year;
     }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(Integer numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ManufacturerEntity getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerEntity manufacturer) {
        this.manufacturer = manufacturer;
    }
}
