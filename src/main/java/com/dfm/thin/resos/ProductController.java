package com.dfm.thin.resos;

import com.dfm.thin.model.Product;
import com.dfm.thin.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api("This is Product API Documentation")
public class ProductController {

    private ProductService service;

    @Autowired
    public void setService(ProductService service) {
        this.service = service;
    }

    @GetMapping(path = "/types/{typeId}/products")
    @ApiOperation("Get products API")
    public List<Product> getTypes(@PathVariable Long typeId) {
        return service.listAllByTypeId(typeId);
    }

    @PostMapping(path = "/products")
    @ApiOperation("Create a product API")
    public Product addType(@Valid @RequestBody Product body) {
        return service.save(body);
    }

    @GetMapping(path = "/products/{id}")
    @ApiOperation("Find product by id")
    public Product getType( @PathVariable Long id ){
        return service.findOne(id);
    }

    @PutMapping(path = "/products/{id}")
    @ApiOperation("Update product by id")
    public Product updateType( @PathVariable Long id, @Valid @RequestBody Product body) {
        return service.update(body, id);
    }

    @DeleteMapping(path = "/products/{id}")
    @ApiOperation("Delete product by id")
    public ResponseEntity<?> deleteType(@PathVariable Long id) {
        return service.delete(id);
    }
}
