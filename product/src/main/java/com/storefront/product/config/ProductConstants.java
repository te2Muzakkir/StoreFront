package com.storefront.product.config;

public final class ProductConstants {

	public ProductConstants() {
		super();
	}
	
	public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201_PRODUCT = "Product Added successfully";
    public static final String  MESSAGE_201_CATEGORY = "Category Added successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";
    public static final String PRODUCT_BY_ID = "product:byId";
    public static final String PRODUCT_BY_CATEGORY = "product:byCategory";
    public static final String PRODUCT_SEARCH = "product:searchByNameOrDesc";
    public static final String PRODUCT_LOAD = "product:load";

}