package com.sukanta.springbootecom.config;

import java.text.DecimalFormat;

public class Constant {
    public static final String USER_EXIST = "User already exists";
    public static final String USER_CREATE_FAILED = "Failed to create a new user";
    public static final String USER_CREATED = "User created successfully";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String PASSWORD_MISMATCHED = "Password not matched. Please check and try again";
    public static final String SESSION_EXPIRED = "Session expired";
    public static final String LOGIN_SUCCESS = "User logged in successfully";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String PRODUCT_ADDED = "Product added successfully";
    public static final String PRODUCTS_FETCH_SUCCESS = "Products fetched successfully";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_UPDATED = "Product updated successfully";
    public static final String CATEGORY_CREATED = "Category created successfully";
    public static final String CATEGORIES_FETCHED = "Categories fetched successfully";
    public static final String UPDATE_AUTHORIZATION_FAILED = "You don't have access to update this product";
    public static final String CATEGORY_UPDATED = "Category updated successfully";
    public static final String PRODUCT_DELETED_SUCCESS = "Product deleted successfully";


    public static float formatToTwoDecimalPlaces(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Float.parseFloat(decimalFormat.format(number));
    }
}
