package com.sukanta.springbootecom.service;

import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.model.Product;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.repository.productRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class productService {

    private final productRepository productRepository;

    public productService(productRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(@NotNull Product request, User user) throws Exception {
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");

        log.info("{} <<== get categories", request.getCategory());

        Product product = Product.builder().name(request.getName()).description(request.getDescription()).sku(request.getSku()).category(request.getCategory()).amount(Float.parseFloat(decimalFormat.format(request.getAmount()))).currency(request.getCurrency()).currSymbol(request.getCurrency().getAbbreviation()).createdBy(user).build();

        if (product != null) {
            return productRepository.save(product);
        } else {
            throw new Exception(Constant.PRODUCT_CREATE_FAILED);
        }

    }

    public List<Product> getProducts(String userId, String searchString) {
//        return searchString == null
//                ? productRepository.getProductsByCreatedByOrderByCreatedAtDesc(userId)
//                : productRepository
//                        .findByCreatedByAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseOrderByCreatedAtDesc(
//                                userId, searchString, searchString);
        log.info("{} user id", userId);
        return productRepository.findAll();
    }

    public List<Product> getAllProducts(String searchString) {
        return productRepository.findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseOrderByCreatedAtDesc(searchString, searchString);
    }

    public Product updateProduct(@NonNull String userId, @NonNull String productId, Product request) throws Exception {
        Product existingProduct = productRepository.findById(productId).get();

        if (existingProduct != null) {
            existingProduct.setName(request.getName());
            existingProduct.setDescription(request.getDescription());
            existingProduct.setSku(request.getSku());

            return productRepository.save(existingProduct);
        } else {
            throw new Exception(Constant.PRODUCT_NOT_FOUND);
        }
    }

    public void deleteProduct(String userId, @NonNull String productId) throws Exception {
        Product existingProduct = productRepository.findById(productId).get();

        if (existingProduct != null) {
            if (!Objects.equals(existingProduct.getCreatedBy().getId(), userId)) {
                throw new Exception(Constant.PRODUCT_NOT_FOUND);
            } else {
                productRepository.deleteById(productId);
            }
        }
    }

    public Product changeStatus(@NotNull String userId, @NotNull String productId) throws Exception {
        Product product = productRepository.findByCreatedByAndId(userId, productId);

        if (product != null) {
            product.setStatus(!product.isStatus());
            return productRepository.save(product);
        } else {
            return null;
        }
    }

}
