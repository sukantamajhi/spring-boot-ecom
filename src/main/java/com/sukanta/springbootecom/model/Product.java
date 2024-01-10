package com.sukanta.springbootecom.model;

import com.sukanta.springbootecom.model.enums.Currency;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    @Hidden
    private String id;
    private String name;
    private String description;
    private double amount;
    private Currency currency;
    private String currSymbol;
    private String sku;
    @Reference
    private List<Category> category;
    @CreatedBy
    @DBRef
    private String createdBy;
    @CreatedDate
    @Hidden
    private Date createdAt;
    @LastModifiedDate
    @Hidden
    private Date updatedAt;
}
