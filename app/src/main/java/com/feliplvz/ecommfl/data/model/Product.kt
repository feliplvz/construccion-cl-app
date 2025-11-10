package com.feliplvz.ecommfl.data.model

import com.feliplvz.ecommfl.data.local.ProductEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    @SerialName("image_url")
    val imageUrl: String,
    val stock: Int,
    val category: String
)

// Funciones de mapeo para Room (compatibilidad)
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
