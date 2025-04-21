package com.unicesumar.view;

import com.unicesumar.controller.ProductController;
import com.unicesumar.controller.SaleController;
import com.unicesumar.controller.UserController;
import com.unicesumar.model.ProductModel;
import com.unicesumar.model.SaleModel;
import com.unicesumar.model.UserModel;
import com.unicesumar.paymentMethods.PaymentManager;
import com.unicesumar.paymentMethods.PaymentMethod;
import com.unicesumar.paymentMethods.PaymentMethodFactory;
import com.unicesumar.paymentMethods.PaymentType;

import java.util.*;

public class SaleView {
    private final Scanner scanner;
    private final SaleController saleController;
    private final UserController userController;
    private final ProductController productController;

    public SaleView(Scanner scanner, SaleController saleController, UserController userController, ProductController productController) {
        this.scanner = scanner;
        this.saleController = saleController;
        this.userController = userController;
        this.productController = productController;
    }

    public void showSaleMenu() {
        int opcao;

        do {
            System.out.println("\n----GERENCIAR VENDAS----");
            System.out.println("1 - Registrar nova venda");
            System.out.println("2 - Listar todas as vendas");
            System.out.println("3 - Buscar venda por ID");
            System.out.println("4 - Deletar venda");
            System.out.println("0 - Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    registrarVenda();
                    break;
                case 2:
                    listarVendas();
                    break;
                case 3:
                    buscarVendaPorId();
                    break;
                case 4:
                    deletarVenda();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    public void registrarVenda() {
        System.out.println("\n--- Registro de Venda ---");

        System.out.print("Email do usuário: ");
        String emailUsuario = scanner.nextLine();

        Optional<UserModel> usuarioOptional = userController.buscarPorEmail(emailUsuario);
        if (usuarioOptional.isEmpty()) {
            System.out.println("Usuário não encontrado com esse email!");
            return;
        }
        UserModel usuario = usuarioOptional.get();
        UUID userId = usuario.getUuid();

        //Fiz isso aqui pra mostrar os produtos pq tava muito ruim de testar, ficou mais bonito no programa final também
        List<ProductModel> todosProdutos = productController.listarProdutos();
        if (todosProdutos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado no sistema.");
            return;
        }

        System.out.println("\nProdutos disponíveis:");
        for (ProductModel produto : todosProdutos) {
            System.out.printf("- %s | R$ %.2f | UUID: %s%n", produto.getName(), produto.getPrice(), produto.getUuid());
        }

        System.out.print("\nDigite os UUIDs dos produtos (separados por vírgula): ");
        String uuidStr = scanner.nextLine();

        List<UUID> productIds = Arrays.stream(uuidStr.split(","))
                .map(String::trim)
                .map(UUID::fromString)
                .toList();

        List<ProductModel> produtosSelecionados = productController.buscarPorIds(productIds);
        if (produtosSelecionados.isEmpty()) {
            System.out.println("Nenhum produto válido foi encontrado.");
            return;
        }

        System.out.println("\nEscolha a forma de pagamento:");
        PaymentType[] tipos = PaymentType.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.printf("%d - %s%n", i + 1, tipos[i].name());
        }

        System.out.print("Opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao < 1 || opcao > tipos.length) {
            System.out.println("Opção inválida.");
            return;
        }

        PaymentType tipoSelecionado = tipos[opcao - 1];

        System.out.println("\nResumo da venda:");
        System.out.println("Cliente: " + usuario.getName());
        System.out.println("Produtos:");
        for (ProductModel p : produtosSelecionados) {
            System.out.printf("- %s (R$ %.2f)%n", p.getName(), p.getPrice());
        }
        double total = produtosSelecionados.stream().mapToDouble(ProductModel::getPrice).sum();
        System.out.printf("Valor total: R$ %.2f%n", total);
        System.out.println("Pagamento: " + tipoSelecionado.name());

        System.out.print("\nDeseja finalizar a venda? (s/n): ");
        String confirmacao = scanner.nextLine().toLowerCase();

        if (!confirmacao.equals("s")) {
            System.out.println("Venda cancelada.");
            return;
        }

        System.out.println("\nAguarde, efetuando pagamento...");
        PaymentMethod metodoPagamento = PaymentMethodFactory.create(tipoSelecionado);
        PaymentManager manager = new PaymentManager();
        manager.setPaymentMethod(metodoPagamento);
        manager.pay(total);
        System.out.println("Pagamento confirmado com sucesso via " + tipoSelecionado.name() + ".");

        SaleModel novaVenda = new SaleModel(userId, tipoSelecionado.name(), produtosSelecionados);
        saleController.criarVenda(userId, produtosSelecionados, tipoSelecionado.name());

        System.out.println("\nVenda registrada com sucesso!");
    }



    private void listarVendas() {
        System.out.println("\n--- Lista de Vendas ---");
        List<SaleModel> vendas = saleController.listarVendas();
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
        } else {
            for (SaleModel venda : vendas) {
                exibirDetalhesVenda(venda);
                System.out.println("-------------------------------");
            }
        }
    }

    private void buscarVendaPorId() {
        System.out.print("Digite o ID da venda: ");
        String id = scanner.nextLine();

        try {
            UUID uuid = UUID.fromString(id);
            Optional<SaleModel> vendaOpt = saleController.encontrarPorId(uuid);
            if (vendaOpt.isPresent()) {
                exibirDetalhesVenda(vendaOpt.get());
            } else {
                System.out.println("Venda não encontrada.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("UUID inválido.");
        }
    }

    private void deletarVenda() {
        System.out.print("Digite o ID da venda a ser deletada: ");
        String id = scanner.nextLine();

        try {
            saleController.deletarVenda(UUID.fromString(id));
            System.out.println("Venda deletada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("UUID inválido.");
        }
    }

    private void exibirDetalhesVenda(SaleModel venda) {
        System.out.println("ID da venda: " + venda.getUuid());
        System.out.println("ID do usuário: " + venda.getUserId());
        System.out.println("Método de pagamento: " + venda.getPaymentMethod());
        System.out.println("Produtos:");

        for (ProductModel p : venda.getProducts()) {
            System.out.printf("  - %s | R$ %.2f%n", p.getName(), p.getPrice());
        }
    }
}
