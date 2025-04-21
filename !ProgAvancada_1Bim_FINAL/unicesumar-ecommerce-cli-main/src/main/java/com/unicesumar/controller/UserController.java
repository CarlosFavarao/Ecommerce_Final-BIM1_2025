package com.unicesumar.controller;

import com.unicesumar.model.UserModel;
import com.unicesumar.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void cadastrarUsuario(String name, String email, String password){
        UserModel user = new UserModel(name, email, password);
        userRepository.save(user);
    }

    public List<UserModel> listarUsuarios(){
        return userRepository.findAll();
    }

    public Optional<UserModel> buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        return userRepository.findByEmail(email);
    }


    public Optional<UserModel> deletarUsuario(UUID id){ //IA me ajudou muito aqui, deletar sem saber o que tá deletando seria terrível
        Optional<UserModel> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()) {
            userRepository.deleteById(id);
        }
        return userOptional;
    }

}
