package com.unicesumar.controller;


import com.unicesumar.model.ProductModel;
import com.unicesumar.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductController {
    private final ProductRepository productRepository;


    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void cadastrarProduto(String name, double price){
        ProductModel product = new ProductModel(name, price);
        productRepository.save(product);
    }

    public List<ProductModel> listarProdutos(){
        return productRepository.findAll();
    }

    public Optional<ProductModel> buscarPorId(UUID id){ //Não utilizado, não consegui pesquisar por vários uuid's...
        if (id == null){
            return Optional.empty();
        }

        return productRepository.findById(id);
    }

    public List<ProductModel> buscarPorIds(List<UUID> ids) { //professor explicou legal, boas práticas aderidas.
        return this.productRepository.findByIds(ids);
    }


    public Optional<ProductModel> deletarProduto(UUID id){
        Optional<ProductModel> productOptuonal = productRepository.findById(id);

        if (productOptuonal.isPresent()){
            productRepository.deleteById(id);
        }
        return productOptuonal;
    }

}
