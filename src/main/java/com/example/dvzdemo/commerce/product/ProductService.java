package com.example.dvzdemo.commerce.product;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return getProduct(id);
    }

    public Product create(String sku, String name, String description, BigDecimal price) {
        Product product = new Product(
            sku,
            name,
            description,
            price
        );
        return productRepository.save(product);
    }

    public Product update(Long id, String sku, String name, String description, BigDecimal price) {
        Product product = getProduct(id);
        product.update(sku, name, description, price);
        return product;
    }

    public void delete(Long id) {
        productRepository.delete(getProduct(id));
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + id));
    }
}
