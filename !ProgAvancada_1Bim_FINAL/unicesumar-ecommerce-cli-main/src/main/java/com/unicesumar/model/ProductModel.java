package com.unicesumar.model;

import java.util.UUID;

public class ProductModel extends Entity {
    private final String name;
    private final double price;

    public ProductModel(UUID uuid, String name, double price) {
        super(uuid);
        this.name = name;
        this.price = price;
    }

    public ProductModel(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return String.format("UUDI: %s | Produto: %s | Preço: %.2f", this.getUuid(), this.name, this.price);
    }
}
