package com.dfm.thin;

import com.dfm.thin.model.ProductType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

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
    public void testApi() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            ProductType p = template.postForObject("http://localhost:8090/api/types", createProductType(), ProductType.class);
            Assert.notNull(p, "Create productType failed");
            Thread.sleep(100);
//            ProductType res = template.getForObject("http://localhost:8090/api/types/{id}", ProductType.class, p.getId());
//            Assert.notNull(res, "Product Type not found");
        }
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
