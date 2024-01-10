package com.sukanta.springbootecom.service;

import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.model.Product;
import com.sukanta.springbootecom.repository.productRepository;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Hidden
public class productService {

    private final productRepository productRepository;
    private final Logger log = LoggerFactory.getLogger(productService.class);

    public productService(productRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product request, String userId) {
        Product product = Product.builder().name(request.getName()).description(request.getDescription()).sku(request.getSku()).category(request.getCategory()).amount(Constant.formatToTwoDecimalPlaces(request.getAmount())).currency(request.getCurrency()).currSymbol(request.getCurrency().getAbbreviation()).createdBy(userId).build();
        return productRepository.save(product);
    }

    public List<Product> getProducts(String userId, String searchString) {
        return searchString == null ? productRepository.getProductsByCreatedByOrderByCreatedAtDesc(userId) : productRepository.findByCreatedByAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseOrderByCreatedAtDesc(userId, searchString, searchString);
    }

    public List<Product> getAllProducts(String searchString) {
        return productRepository.findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseOrderByCreatedAtDesc(searchString, searchString);
    }

    public Product updateProduct(String userId, String productId, Product request) throws Exception {
        Product existingProduct = productRepository.findById(productId).orElse(null);

        if (existingProduct != null) {
            existingProduct.setName(request.getName());
            existingProduct.setDescription(request.getDescription());
            existingProduct.setSku(request.getSku());

            return productRepository.save(existingProduct);
        } else {
            throw new Exception(Constant.PRODUCT_NOT_FOUND);
        }
    }

    public void deleteProduct(String userId, String productId) throws Exception {
        Product existingProduct = productRepository.findById(productId).orElse(null);

        if (existingProduct != null) {
            if (!Objects.equals(existingProduct.getCreatedBy(), userId)) {
                throw new Exception(Constant.PRODUCT_NOT_FOUND);
            } else {
                productRepository.deleteById(productId);
            }
        }
    }

}