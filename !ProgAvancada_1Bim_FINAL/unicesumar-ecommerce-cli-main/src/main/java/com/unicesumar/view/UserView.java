package com.unicesumar.view;

import com.unicesumar.controller.UserController;
import com.unicesumar.model.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class UserView {
    private final UserController userController;
    private final Scanner scanner;

    public UserView(UserController userController, Scanner scanner){
        this.userController = userController;
        this.scanner = scanner;
    }

    public void showUserMenu(){
        int opcao;
        do{
            System.out.println("\n----GERENCIAR USUÁRIOS----");
            System.out.println("1 - Cadastrar Usuário");
            System.out.println("2 - Listar Usuários");
            System.out.println("3 - Buscar Usuários (Email)");
            System.out.println("4 - Deletar Usuário (UUID)");
            System.out.println("0 - Voltar para o Menu");
            System.out.println("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao){
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    listarUsuarios();
                    break;
                case 3:
                    buscarUsuarioEmail();
                    break;
                case 4:
                    deletarUsuarioUuid();
                    break;
                case 0:
                    System.out.println("Retornando ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente...");
                    break;
            }
        }while(opcao != 0);
    }

    private void cadastrarUsuario(){
        System.out.println("Nome: ");
        String name = scanner.nextLine();

        System.out.println("Email: ");
        String email = scanner.nextLine();

        System.out.println("Senha: ");
        String password = scanner.nextLine();

        userController.cadastrarUsuario(name, email, password);
        System.out.println("Usuário cadastrado!");
    }

    private void listarUsuarios(){
        List<UserModel> usuarios = userController.listarUsuarios(); //Sei que isso causa uma carga terrível em
                                                                    //larga escala mas, não estamos em larga escala então...
        if (usuarios.isEmpty()){
            System.out.println("Nenhum Usuário registrado.");
        }else{
            System.out.println("\n---Lista de Usuários cadastrados---");
            for (UserModel user : usuarios) {
                System.out.println(user);
            }
        }
    }

    private void buscarUsuarioEmail(){
        System.out.println("\nDigite o Email: ");
        String email = scanner.nextLine();

        Optional<UserModel> user = userController.buscarPorEmail(email);

        if (user.isPresent()){
            System.out.println("Usuário encontrado!");
            System.out.println(user.get());
        }else{
            System.out.println("Usuário não encontrado...");
        }
    }

    private void deletarUsuarioUuid(){
        System.out.println("Digite o UUID do usuário a ser deletado: ");
        String idDeletar = scanner.nextLine();

        try {
            UUID uuid = UUID.fromString(idDeletar);
            Optional<UserModel> user = userController.deletarUsuario(uuid);

            if (user.isPresent()) {
                System.out.println("Usuário Deletado: " + user.get());
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("UUID inválido.");
        }
    }
}
