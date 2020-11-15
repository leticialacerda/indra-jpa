package com.manager.fuel.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class FuelHistory {

    @Id
    @GeneratedValue
    private Long id;

    private String region;

    private String state;

    private String city;

    private String resale;

    private String codInstallation;

    private String product;

    private Date dateCollect;

    private BigDecimal valuePurchase;

    private BigDecimal valueSale;

    private String unit;

    private String brand;

    public FuelHistory() {
    }

    public FuelHistory(String region, String state, String city, String resale, String codInstallation, String product, Date dateCollect, BigDecimal valuePurchase, BigDecimal valueSale, String unit, String brand) {
        this.region = region;
        this.state = state;
        this.city = city;
        this.resale = resale;
        this.codInstallation = codInstallation;
        this.product = product;
        this.dateCollect = dateCollect;
        this.valuePurchase = valuePurchase;
        this.valueSale = valueSale;
        this.unit = unit;
        this.brand = brand;
    }
}
