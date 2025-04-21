package com.unicesumar.repository;

import com.unicesumar.model.ProductModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ProductRepository implements EntityRepository<ProductModel> {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(ProductModel entity) {
        String query = "INSERT INTO products VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, entity.getUuid().toString());
            stmt.setString(2, entity.getName());
            stmt.setDouble(3, entity.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ProductModel> findById(UUID id) {
        String query = "SELECT * FROM products WHERE uuid = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                ProductModel product = new ProductModel(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    // select * from products where uuid in (?,?,?) (boa prática)

    public List<ProductModel> findByIds(List<UUID> ids){
        if (ids == null || ids.isEmpty()){
            return Collections.emptyList();
        }

        //Faz a quantidade de ? ser do tamanho certo, afinal, nada é automático.
        String placeholders = ids.stream().map(id -> "?").collect(Collectors.joining(", "));

        String query = "SELECT * FROM products WHERE uuid IN (" + placeholders + ")";
        List<ProductModel> produtos = new ArrayList<>();


        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);

             for (int i = 0; i < ids.size(); i++) {
                 stmt.setString(i + 1,ids.get(i).toString());
             }

             ResultSet rset = stmt.executeQuery();

             while (rset.next()){
                 ProductModel produto =  new ProductModel(
                         UUID.fromString(rset.getString("uuid")),
                         rset.getString("name"),
                         rset.getDouble("price")
                 );
                 produtos.add(produto);
             }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return produtos;
    }

    @Override
    public List<ProductModel> findAll() {
        String query = "SELECT * FROM products";
        List<ProductModel> products = new LinkedList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                ProductModel product = new ProductModel(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;

    }

    @Override
    public void deleteById(UUID id) {
        String query = "DELETE FROM products WHERE uuid = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
