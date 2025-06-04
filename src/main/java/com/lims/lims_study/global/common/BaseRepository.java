package com.lims.lims_study.global.common;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {
    void insert(T entity);

    void update(T entity);

    void delete(ID id);

    Optional<T> findById(ID id);

    List<T> findAll();
}

