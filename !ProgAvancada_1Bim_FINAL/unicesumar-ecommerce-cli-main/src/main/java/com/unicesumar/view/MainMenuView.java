package com.unicesumar.view;

import java.util.Scanner;

public class MainMenuView {
    private final Scanner scanner;
    private final UserView userView;
    private final ProductView productView;
    private final SaleView saleView;

    public MainMenuView(UserView userView, ProductView productView, SaleView saleView, Scanner scanner) {
        this.userView = userView;
        this.productView = productView;
        this.saleView = saleView;
        this.scanner = scanner;
    }

    public void start() {
        int opcao;
        do {
            System.out.println("\n---- MENU PRINCIPAL ----");
            System.out.println("1 - Gerenciar Usuários");
            System.out.println("2 - Gerenciar Produtos");
            System.out.println("3 - Vendas");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Digite um número válido.");
                scanner.next();
                System.out.print("Escolha uma opção: ");
            }

            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    userView.showUserMenu();
                    break;
                case 2:
                    productView.showProductMenu();
                    break;
                case 3:
                    saleView.showSaleMenu();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
}
