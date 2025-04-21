package com.unicesumar.repository;

import com.unicesumar.model.ProductModel;
import com.unicesumar.model.SaleModel;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SaleRepository implements EntityRepository<SaleModel> {
    private final Connection connection;

    public SaleRepository(Connection connection) {
        this.connection = connection;
    }

    //Salvar vendas
    public SaleModel save(SaleModel sale, List<ProductModel> products) {
        String salesQuery = "INSERT INTO sales (id, user_id, payment_method, sale_date) VALUES (?, ?, ?, ?)";
        String saleProductsQuery = "INSERT INTO sale_products (sale_id, product_id) VALUES (?, ?)";

        try {
            //Iniciando a transação não deixando ele comitar automaticamente no BD
            this.connection.setAutoCommit(false);

            PreparedStatement saleStmt = this.connection.prepareStatement(salesQuery, Statement.RETURN_GENERATED_KEYS);
            saleStmt.setString(1, sale.getUuid().toString());
            saleStmt.setString(2, sale.getUserId().toString());
            saleStmt.setString(3, sale.getPaymentMethod());
            saleStmt.setString(4, sale.getSaleDate().toString());
            saleStmt.executeUpdate();

            UUID saleId = sale.getUuid();

            //Associação dos produtos na sale
            PreparedStatement saleProductStmt = this.connection.prepareStatement(saleProductsQuery);
            for (ProductModel product : products) {
                saleProductStmt.setString(1, saleId.toString());
                saleProductStmt.setString(2, product.getUuid().toString());
                saleProductStmt.addBatch();
            }

            //ENTENDER DEPOIS mas executa a venda //entendi, batch é lote em inglês
            saleProductStmt.executeBatch();

            this.connection.commit();
            this.connection.setAutoCommit(true);

            return new SaleModel(saleId, sale.getUserId(), sale.getPaymentMethod(), sale.getSaleDate(), products);


        } catch (SQLException e) {
            try {
                this.connection.rollback(); //se der ruim, volta
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao salvar a venda: " + e.getMessage());
        }
    }

    //Tive que implementar por obrigação, mas nesse caso em específico não deu pra implementar com Entity (precisava da sale e da lista)
    @Override
    public void save(SaleModel entity) {
        throw new UnsupportedOperationException("Use o método save(SaleModel sale, List<ProductModel> products)");
    }


    @Override
    public Optional<SaleModel> findById(UUID id) {
        String query = "SELECT * FROM sales WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                UUID userId = UUID.fromString(resultSet.getString("user_id"));
                String paymentMethod = resultSet.getString("payment_method");
                LocalDateTime saleDate = LocalDateTime.parse(resultSet.getString("sale_date"));

                List<ProductModel> products = findProductsBySaleId(id);
                return Optional.of(new SaleModel(id, userId, paymentMethod, saleDate, products));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar a venda: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<SaleModel> findAll() {
        List<SaleModel> sales = new ArrayList<>();
        String query = "SELECT * FROM sales";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                UUID userId = UUID.fromString(resultSet.getString("user_id"));
                String paymentMethod = resultSet.getString("payment_method");
                LocalDateTime saleDate = LocalDateTime.parse(resultSet.getString("sale_date"));

                //busca os produtos da venda e monta o model completo
                List<ProductModel> products = findProductsBySaleId(id);
                sales.add(new SaleModel(id, userId, paymentMethod, saleDate, products));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as vendas: " + e.getMessage());
        }

        return sales;
    }


    @Override
    public void deleteById(UUID id) {
        String deleteSaleProducts = "DELETE FROM sale_products WHERE sale_id = ?";
        String deleteSale = "DELETE FROM sales WHERE id = ?";

        try {
            this.connection.setAutoCommit(false);

            //remove associações dos produtos
            PreparedStatement deleteProductsStmt = this.connection.prepareStatement(deleteSaleProducts);
            deleteProductsStmt.setString(1, id.toString());
            deleteProductsStmt.executeUpdate();

            //remove a venda em si
            PreparedStatement deleteSaleStmt = this.connection.prepareStatement(deleteSale);
            deleteSaleStmt.setString(1, id.toString());
            deleteSaleStmt.executeUpdate();

            this.connection.commit();
            this.connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                this.connection.rollback(); // volta se der erro
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao deletar a venda: " + e.getMessage());
        }
    }


    private List<ProductModel> findProductsBySaleId(UUID saleId) {
        List<ProductModel> products = new ArrayList<>();
        String query = "SELECT p.uuid, p.name, p.price FROM products p " +
                "JOIN sale_products sp ON p.uuid = sp.product_id WHERE sp.sale_id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, saleId.toString());
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
            throw new RuntimeException("Erro ao buscar produtos da venda: " + e.getMessage());
        }
        return products;
    }
}
