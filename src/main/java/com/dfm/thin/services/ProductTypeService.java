package com.dfm.thin.services;

import com.dfm.thin.model.ProductType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductTypeService {

    List<ProductType> listAll();

    ProductType findOne(Long id);

    ProductType save(ProductType productType);

    ProductType update(ProductType body, Long id);

    ResponseEntity<?> delete(Long id);

}
