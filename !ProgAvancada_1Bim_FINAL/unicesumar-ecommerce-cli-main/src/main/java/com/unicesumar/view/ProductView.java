package com.unicesumar.view;

import com.unicesumar.controller.ProductController;
import com.unicesumar.model.ProductModel;

import java.util.*;

public class ProductView{
    private final ProductController productController;
    private final Scanner scanner;

    public ProductView(ProductController productController, Scanner scanner) {
        this.productController = productController;
        this.scanner = scanner;
    }

    public void showProductMenu(){
        int opcao;
        do {
            System.out.println("\n----GERENCIAR PRODUTOS----");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("3 - Buscar Produtos por UUID");
            System.out.println("4 - Deletar Produto por UUID");
            System.out.println("0 - Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch(opcao){
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    buscarProdutosPorUuids();
                    break;
                case 4:
                    deletarProduto();
                    break;
                case 0:
                    System.out.println("Retornando ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente...");
                    break;
            }
        } while (opcao != 0);
    }

    private void cadastrarProduto() {
        System.out.print("\nNome do Produto: ");
        String nome = scanner.nextLine();
        scanner.nextLine();

        System.out.print("Preço do Produto: ");
        double preco = Double.parseDouble(scanner.nextLine());

        productController.cadastrarProduto(nome, preco);
        System.out.println("Produto cadastrado com sucesso!");
    }

    private void listarProdutos() {
        List<ProductModel> produtos = productController.listarProdutos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println("\n----Lista de Produtos----");
            for (ProductModel produto : produtos) {
                System.out.println(produto);
            }
        }
    }

    private void buscarProdutosPorUuids() {
        System.out.println("Digite o UUID (caso precise buscar vários produtos separe os UUIDs por vírgula): ");
        String uuids = scanner.nextLine();

        String[] partes = uuids.split(",");
        List<UUID> ids = new ArrayList<>();

        for (String parte : partes) {
            try {
                UUID uuid = UUID.fromString(parte.trim());
                ids.add(uuid);
            } catch (Exception e) {
                System.out.println("UUID inválido: " + parte.trim() + " removido da busca.");
            }
        }
        if (ids.isEmpty()){
            System.out.println("Nenhum UUID foi informado.");
            return;
        }

        List<ProductModel> produtos = productController.buscarPorIds(ids);

        if (produtos.isEmpty()){
            System.out.println("Nenhum produto encontrado com o(s) UUID(s) informado(s).");
        } else {
            System.out.println("\n----Produtos Encontrados----");
            for (ProductModel p : produtos){
                System.out.println(p);
            }
        }
    }

    private void deletarProduto() {
        System.out.print("\nDigite o UUID do produto a ser deletado: ");
        String idString = scanner.nextLine();

        try {
            UUID id = UUID.fromString(idString);
            Optional<ProductModel> produto = productController.deletarProduto(id);

            if (produto.isPresent()) {
                System.out.println("Produto deletado: " + produto.get());
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("UUID inválido.");
        }
    }
}

