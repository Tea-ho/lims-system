package com.lims.lims_study.domain.product.repository;

import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductRepository extends BaseRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> searchByName(String name);
}