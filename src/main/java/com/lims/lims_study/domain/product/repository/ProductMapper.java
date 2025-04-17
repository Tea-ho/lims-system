package com.lims.lims_study.domain.product.repository;

import com.lims.lims_study.domain.product.model.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {
    void insert(Product product);
    void update(Product product);
    void delete(Long id);
    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    List<Product> searchByName(String name);
}