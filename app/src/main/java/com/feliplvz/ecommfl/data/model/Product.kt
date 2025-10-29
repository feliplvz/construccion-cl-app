package com.feliplvz.ecommfl.data.model

import com.feliplvz.ecommfl.data.local.ProductEntity

data class Product(
    val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val stock: Int,
    val category: String
)

// Funciones de mapeo
fun ProductEntity.toProduct() = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl,
    stock = stock,
    category = category
)

fun Product.toEntity() = ProductEntity(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl,
    stock = stock,
    category = category
)
