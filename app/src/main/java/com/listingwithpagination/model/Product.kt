package com.listingwithpagination.model

import java.math.BigInteger


data class Product(
    val id: BigInteger,
    val rating: Float,
    val image_url: String,
    val final_price: Int,
    val show_wishlist_button: Int,
    val quantity: Int,
    val button_text: String,
    val name: String,
    val rating_count: Int
)