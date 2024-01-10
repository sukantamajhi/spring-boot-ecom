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
@Document(collection = "categories")
public class Category {
    @Id
    @Hidden
    private String id;
    private String name;
    private String description;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    @Hidden
    private Date createdAt;
    @LastModifiedDate
    @Hidden
    private Date updatedBy;
}
