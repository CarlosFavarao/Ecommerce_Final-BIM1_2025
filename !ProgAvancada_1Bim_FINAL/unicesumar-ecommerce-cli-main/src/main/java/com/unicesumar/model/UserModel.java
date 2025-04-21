package com.unicesumar.model;

import java.util.UUID;

public class UserModel extends Entity {
    private String name;
    private String email;
    private String password;

    public UserModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserModel(UUID uuid, String name, String email, String password) {
        super(uuid);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("UUID: %s | Nome: %s | Email: %s",this.getUuid(), this.name, this.email);
    }
}
