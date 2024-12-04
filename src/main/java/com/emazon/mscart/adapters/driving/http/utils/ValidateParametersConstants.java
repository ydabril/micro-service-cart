package com.emazon.mscart.adapters.driving.http.utils;

public class ValidateParametersConstants {
    public static final String PAGE_NUMBER_NEGATIVE = "Page number cannot be negative";
    public static final String PAGE_SIZE_INVALID = "Page size must be greater than zero";
    public static final String INVALID_SORT_DIRECTION = "Invalid sort direction. Must be 'ASC' or 'DESC'";
    public static final String INVALID_SORT_PROPERTY = "Invalid sort property";
    public static final String SORT_DIRECTION_PATTERN =  "ASC" + "|" + "DESC";
    public static final String SORT_BY_PATTERN = "ARTICLE_NAME" + "|" + "BRAND_NAME" +  "|" + "CATEGORIES";

    // Enum para SortDirection
    public enum SortDirection {
        ASC,
        DESC
    }

    // Enum para SortBy
    public enum SortBy {
        ARTICLE_NAME,
        BRAND_NAME,
        CATEGORIES
    }
}
