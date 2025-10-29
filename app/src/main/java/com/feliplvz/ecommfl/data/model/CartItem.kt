package com.feliplvz.ecommfl.data.model

import com.feliplvz.ecommfl.data.local.CartItemEntity

data class CartItem(
    val id: Long = 0,
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val productImage: String,
    val quantity: Int
)

fun CartItemEntity.toCartItem() = CartItem(
    id = id,
    productId = productId,
    productName = productName,
    productPrice = productPrice,
    productImage = productImage,
    quantity = quantity
)

fun CartItem.toEntity() = CartItemEntity(
    id = id,
    productId = productId,
    productName = productName,
    productPrice = productPrice,
    productImage = productImage,
    quantity = quantity
)

