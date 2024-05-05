package com.eliferden.onlineshoppingwebsite.exceptions;

public class ErrorMessagesForProduct {
    public static final String PRODUCT_NAME_TAKEN = "Product with name %s already exists.";
    public static final String PRODUCT_NOT_FOUND = "Product not found.";
    public static final String PRODUCT_NOT_FOUND_WITH_ID = "Product not found with id: %s"; // %s will be replaced by Product id
    public static final String PRODUCT_NAME_EMPTY = "Product name cannot be empty.";
    public static final String PRICE_VALUE_INVALID = "Price must be greater than zero.";
    public static final String STOCK_VALUE_INVALID = "Stock cannot be negative.";


}
