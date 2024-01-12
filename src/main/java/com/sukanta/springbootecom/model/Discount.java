package com.sukanta.springbootecom.model;

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

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "discounts")
public class Discount {
    @Id
    @Hidden
    private String id;
    private String name;
    private String description;
    private float amount;
    private float percentage;
    @Builder.Default
    private String productId = null;
    @Builder.Default
    @Hidden
    private boolean status = true;
    private Date validFrom;
    private Date validTo;
    @CreatedBy
    @Hidden
    private String createdBy;
    @CreatedDate
    @Hidden
    private Date createdAt;
    @LastModifiedDate
    @Hidden
    private Date updatedAt;
}
