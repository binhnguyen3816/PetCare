package com.binh.utils;

import java.util.List;

public interface BaseDAO<T, ID> {
    T findById(ID id);

    List<T> findAll();

    T save(T entity);

    T update(T entity);

    void delete(ID id);
}