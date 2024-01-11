package com.sukanta.springbootecom.model.user;

import com.sukanta.springbootecom.model.enums.Role;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    @Hidden
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    @Builder.Default
    @Hidden
    private boolean active = true;
    @Field("role")
    @Hidden
    private Role role;
    @Field("password")
    private String password;
    @CreatedDate
    @Hidden
    private Date createdAt;
    @LastModifiedDate
    @Hidden
    private Date updatedAt;
}
