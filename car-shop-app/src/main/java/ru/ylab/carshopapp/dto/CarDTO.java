package ru.ylab.carshopapp.dto;

import lombok.Data;

@Data
public class CarDTO {
    private String vinNumber;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String condition;

    public CarDTO(String vinNumber, String brand, String model, int year, double price, String condition) {
        validateVin(vinNumber);
        validateBrand(brand);
        validateModel(model);
        validateCondition(condition);

        this.vinNumber = vinNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.condition = condition;
    }

    private void validateVin(String vin) {
        if (vin == null || vin.length() != 17) {
            throw new IllegalArgumentException("VIN must be exactly 17 characters long");
        }
    }

    private void validateBrand(String brand) {
        if (brand == null || brand.isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be null or empty");
        }
    }

    private void validateModel(String model) {
        if (model == null || model.isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
    }

    private void validateCondition(String condition) {
        if (condition == null || condition.isEmpty()) {
            throw new IllegalArgumentException("Condition cannot be null or empty");
        }
    }
}
