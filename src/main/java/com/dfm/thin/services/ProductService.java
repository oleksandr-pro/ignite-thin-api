package com.dfm.thin.services;

import com.dfm.thin.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    List<Product> listAllByTypeId(Long typeId);

    Product findOne(Long id);

    Product save(Product productType);

    Product update(Product body, Long id);

    ResponseEntity<?> delete(Long id);
}
