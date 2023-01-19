package com.listingwithpagination.model

data class ProductResponse(
    val product_count: Int,
    val total_found: Int,
    val products: List<Product>
)