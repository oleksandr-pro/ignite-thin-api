package com.dfm.thin.services;


import com.dfm.thin.model.ProductType;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientException;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Service
public class ProductTypeServiceImpl implements ProductTypeService{

    final String CACHE_NAME = "ProductTypeCache";
    private static final AtomicLong ID_GEN = new AtomicLong();

    @Override
    public List<ProductType> listAll() {
        List<ProductType> productTypes = new ArrayList<>();
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");

        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, ProductType> cache = client.getOrCreateCache(CACHE_NAME);

            String sql = "select * from product_type";
            List<List<?>> res = cache.query(new SqlFieldsQuery(sql).setDistributedJoins(true)).getAll();

            for (List<?> next : res) {
                ProductType productType = new ProductType();
                productType.setCreatedAt((Timestamp) next.get(0));
                productType.setUpdatedAt((Timestamp) next.get(1));
                productType.setName((String) next.get(2));
                productType.setDescription((String) next.get(3));
                productTypes.add(productType);
            }
            return productTypes;
        }
        catch (ClientException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.format("Unexpected failure: %s\n", e);
        }

        return null;
    }

    @Override
    public ProductType findOne(Long id) {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, ProductType> cache = client.getOrCreateCache(CACHE_NAME);
            return cache.get(id);
        }
        catch (ClientException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.format("Unexpected failure: %s\n", e);
        }
        return null;
    }

    @Override
    public ProductType save(ProductType productType) {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, ProductType> cache = client.getOrCreateCache(CACHE_NAME);
            Long key =  generateId();
            cache.put(key, productType);
            return cache.get(key);
        }
        catch (ClientException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.format("Unexpected failure: %s\n", e);
        }
        return null;
    }

    @Override
    public ProductType update(ProductType body, Long id) {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, ProductType> cache = client.getOrCreateCache(CACHE_NAME);
            cache.replace(id, body);
            return cache.get(id);
        }
        catch (ClientException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.format("Unexpected failure: %s\n", e);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, ProductType> cache = client.getOrCreateCache(CACHE_NAME);

            cache.remove(id);
            return ResponseEntity.ok().build();
        }
        catch (ClientException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.format("Unexpected failure: %s\n", e);
        }
        return null;
    }

    private Long generateId() {
        return ID_GEN.incrementAndGet();
    }
}
