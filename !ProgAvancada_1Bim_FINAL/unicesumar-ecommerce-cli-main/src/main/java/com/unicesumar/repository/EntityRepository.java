package com.unicesumar.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntityRepository<T> {
    void save(T entity);
    Optional<T> findById(UUID id);
    List<T> findAll();
    void deleteById(UUID id);
}
