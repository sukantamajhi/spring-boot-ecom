package com.sukanta.springbootecom.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "coupons")
public class Coupon {
    @Id
    @Hidden
    private String id;
    private String productId;
    private float amount;
    @CreatedDate
    @Hidden
    private Date createdAt;
    @LastModifiedDate
    @Hidden
    private Date updatedAt;
}
