package com.unicesumar.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaleModel extends Entity {
    private final UUID userId;
    private final String paymentMethod;
    private final LocalDateTime saleDate;
    private final List<ProductModel> products;

    //Criação
    public SaleModel(UUID uuid, UUID userId, String paymentMethod, LocalDateTime saleDate, List<ProductModel> products) {
        super(uuid);
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.saleDate = saleDate;
        this.products = products;
    }

    //Busca
    public SaleModel(UUID userId, String paymentMethod, List<ProductModel> products) {
        this(UUID.randomUUID(), userId, paymentMethod, LocalDateTime.now(), products);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        List<UUID> productIds = new ArrayList<>();
        for (ProductModel p : products) {
            productIds.add(p.getUuid());
        }
        return String.format("Venda: %s | Usuário: %s | Pagamento: %s | Data: %s | Produtos: %s",
                this.getUuid(), userId, paymentMethod, saleDate, productIds);
    }
}
