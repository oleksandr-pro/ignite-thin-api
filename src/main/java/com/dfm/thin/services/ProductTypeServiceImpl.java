package com.dfm.thin.services;


import com.dfm.thin.model.ProductType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class ProductTypeServiceImpl implements ProductTypeService{


    @Override
    public List<ProductType> listAll() {
        return null;
    }

    @Override
    public ProductType findOne(Long id) {
        return null;
    }

    @Override
    public ProductType save(ProductType productType) {
        return null;
    }

    @Override
    public ProductType update(ProductType body, Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        return null;
    }
}
