package com.unicesumar;

import com.unicesumar.controller.ProductController;
import com.unicesumar.controller.SaleController;
import com.unicesumar.controller.UserController;
import com.unicesumar.repository.ProductRepository;
import com.unicesumar.repository.SaleRepository;
import com.unicesumar.repository.UserRepository;
import com.unicesumar.view.MainMenuView;
import com.unicesumar.view.ProductView;
import com.unicesumar.view.SaleView;
import com.unicesumar.view.UserView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:database.sqlite";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);

        ProductController productController = new ProductController(new ProductRepository(conn));
        UserController userController = new UserController(new UserRepository(conn));
        SaleController saleController = new SaleController(new SaleRepository(conn));

        UserView userView = new UserView(userController, scanner);
        ProductView productView = new ProductView(productController, scanner);
        SaleView saleView = new SaleView(scanner, saleController, userController, productController);

        MainMenuView mainMenuView = new MainMenuView(userView, productView, saleView, scanner);
        mainMenuView.start();

        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexão: " + e.getMessage());
        }

        scanner.close();
    }
}
