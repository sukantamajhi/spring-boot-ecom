package com.sukanta.springbootecom.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sukanta.springbootecom.model.enums.Currency;
import com.sukanta.springbootecom.model.user.User;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String discountId;
    @Builder.Default
    private boolean status = false;
    @Reference
    private List<Category> category;
    @CreatedBy
    @DBRef
    private User createdBy;
    @CreatedDate
    @Hidden
    private Date createdAt;
    @LastModifiedDate
    @Hidden
    private Date updatedAt;
}
