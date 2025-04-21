package com.unicesumar.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.unicesumar.model.Entity;

public class InMemoryRepository<T extends Entity> implements EntityRepository<T> {
    private ArrayList<T> dataStore = new ArrayList<>();

    @Override
    public void save(T entity) {
        this.dataStore.removeIf(e -> e.getUuid().equals(entity.getUuid()));
        this.dataStore.add(entity);
    }

    @Override
    public Optional<T> findById(UUID id) {
        return this.dataStore.stream().filter(e -> e.getUuid().equals(id)).findFirst();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(this.dataStore);
    }

    @Override
    public void deleteById(UUID id) {
        this.dataStore.removeIf(e -> e.getUuid().equals(id));
    }
}
