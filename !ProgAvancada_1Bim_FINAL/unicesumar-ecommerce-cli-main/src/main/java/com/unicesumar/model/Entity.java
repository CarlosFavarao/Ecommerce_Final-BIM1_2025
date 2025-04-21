package com.unicesumar.model;

import java.util.UUID;

public abstract class Entity {
    private UUID uuid;

    public Entity() {
        this.uuid = UUID.randomUUID();
    }

    public Entity(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
