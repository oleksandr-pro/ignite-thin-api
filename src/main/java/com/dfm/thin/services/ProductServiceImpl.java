package com.dfm.thin.services;

import com.dfm.thin.exceptions.ResourceNotFoundException;
import com.dfm.thin.model.Product;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientException;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Service
public class ProductServiceImpl implements ProductService{

    final String PRODUCT_CACHE = "ProductCache";
    final String TYPE_CACHE = "ProductCache";
    private static final AtomicLong ID_GEN = new AtomicLong();
    @Override
    public List<Product> listAllByTypeId(Long typeId) {

        List<Product> products = new ArrayList<>();
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");

        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, Product> cache = client.getOrCreateCache(PRODUCT_CACHE);

            String sql = "select id, name, description, url, type_id from product where type_id = ?";
            List<List<?>> res = cache.query(new SqlFieldsQuery(sql).setArgs(typeId).setDistributedJoins(true)).getAll();

            for (List<?> next : res) {
                Product p = new Product();
                p.setId((Long) next.get(0));
                p.setName((String) next.get(1));
                p.setDescription((String) next.get(2));
                p.setUrl((String) next.get(3));
                p.setTypeId((Long) next.get(4));
                products.add(p);
            }
            return products;
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
    public Product findOne(Long id) {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, Product> cache = client.getOrCreateCache(PRODUCT_CACHE);
            Product p = cache.get(id);
            if (p == null) {
                throw new ResourceNotFoundException("Product not found with id " + id);
            }
            p.setId(id);
            return p;
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
    public Product save(Product body) {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, Product> cache = client.getOrCreateCache(PRODUCT_CACHE);
            String sql = "select max(id) + 1 from product";
            Long key = (Long) cache.query(new SqlFieldsQuery(sql).setDistributedJoins(true)).getAll().iterator().next().iterator().next();
            if (key == null) {
                key = 1L;
            }
            body.setId(key);
            cache.put(key, body);
            return body;
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
    public Product update(Product body, Long id) {

        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient client = Ignition.startClient(cfg)) {
            ClientCache<Long, Product> cache = client.getOrCreateCache(PRODUCT_CACHE);
            body.setId(id);
            Product p = cache.getAndPut(id, body);
            if (p == null) {
                throw new ResourceNotFoundException("Product not found with id " + id);
            }
            return body;
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
            ClientCache<Long, Product> cache = client.getOrCreateCache(PRODUCT_CACHE);

            Product p = cache.getAndRemove(id);
            if (p == null) {
                throw new ResourceNotFoundException("Product not found with id " + id);
            }
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
}
