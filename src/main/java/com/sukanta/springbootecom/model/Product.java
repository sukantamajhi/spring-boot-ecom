package com.sukanta.springbootecom.model;

import com.sukanta.springbootecom.model.enums.Currency;
import com.sukanta.springbootecom.model.user.User;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
    private String discountId;
    @Builder.Default
    private boolean status = false;
    @DocumentReference
    private List<Category> category;
    @CreatedBy
    @DocumentReference
    private User createdBy;
    @CreatedDate
    @Hidden
    private Date createdAt;
    @LastModifiedDate
    @Hidden
    private Date updatedAt;
}
