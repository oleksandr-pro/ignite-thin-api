package com.dfm.thin.resos;

import com.dfm.thin.model.ProductType;
import com.dfm.thin.services.ProductTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api("This is Product Type API Documentation")
public class ProductTypeController {

    private ProductTypeService service;

    @Autowired
    public void setService(ProductTypeService service) {this.service = service;}

    @GetMapping(path = "/types")
    @ApiOperation("Get types API")
    public List<ProductType> getTypes() {
        return service.listAll();
    }

    @PostMapping(path = "/types")
    @ApiOperation("Create a type API")
    public ProductType addType(@Valid @RequestBody ProductType body) {
        return service.save(body);
    }

    @GetMapping(path = "/types/{id}")
    @ApiOperation("Find type by id")
    public ProductType getType(@PathVariable Long id ){
        return service.findOne(id);
    }

    @PutMapping(path = "/types/{id}")
    @ApiOperation("Update type by id")
    public ProductType updateType(@PathVariable Long id, @Valid @RequestBody ProductType body) {
        return service.update(body, id);
    }

    @DeleteMapping(path = "/types/{id}")
    @ApiOperation("Delete type by id")
    public ResponseEntity<?> deleteType(@PathVariable Long id) {
        return service.delete(id);
    }

}
