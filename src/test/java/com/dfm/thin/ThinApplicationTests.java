package com.dfm.thin;

import com.dfm.thin.model.Product;
import com.dfm.thin.model.ProductType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThinApplicationTests {

    TestRestTemplate template = new TestRestTemplate();
    Random r = new Random();
    int[] clusterPorts = new int[] {8090};

    @Test
    public void contextLoads() {
    }

    @Test
    public void testProductTypeApi() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            ProductType p = template.postForObject("http://localhost:8090/api/types", createProductType(), ProductType.class);
//            Assert.notNull(p, "Create productType failed");
            Thread.sleep(100);
//            ProductType res = template.getForObject("http://localhost:8090/api/types/{id}", ProductType.class, p.getId());
//            Assert.notNull(res, "Product Type not found");
        }
    }

    @Test
    public void testProductApi() throws InterruptedException {
        ResponseEntity<List<ProductType>> response = template.exchange(
                "http://localhost:8090/api/types",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductType>>(){});
        List<ProductType> productTypes = response.getBody();
        for (ProductType productType: productTypes) {
            for (int i = 0; i < 5; i++) {
                Product product = template.postForObject("http://localhost:8090/api/products", createProduct(productType.getId()), Product.class);
                Assert.notNull(product, "Create product failed");
                Thread.sleep(100);
            }

        }

    }

    private Product createProduct(Long typeId) {
        Product p = new Product();
        Random r = new Random();
        int n = r.nextInt(100000);
        p.setName("Test Product name" + n);
        p.setId(1L);
        p.setTypeId(typeId);
        p.setUrl("Test Product url" + n);
        p.setDescription("Test Product description" + n);
        return p;
    }

    private ProductType createProductType() {
        ProductType p = new ProductType();
        Random r = new Random();
        int n =  r.nextInt(100000);
        p.setName("Test name" + n);
        p.setId(1L);
        p.setDescription("Test Description" + n);
        return p;
    }

}
