package com.unicesumar.controller;

import com.unicesumar.model.ProductModel;
import com.unicesumar.model.SaleModel;
import com.unicesumar.repository.SaleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SaleController {
    private final SaleRepository saleRepository;

    public SaleController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public SaleModel criarVenda(UUID userId, List<ProductModel> products, String paymentMethod) {
        SaleModel sale = new SaleModel(userId, paymentMethod, products);
        return saleRepository.save(sale, products);
    }

    public Optional<SaleModel> encontrarPorId(UUID id) {
        return saleRepository.findById(id);
    }

    public List<SaleModel> listarVendas() {
        return saleRepository.findAll();
    }

    public void deletarVenda(UUID id) {
        saleRepository.deleteById(id);
    }
}
