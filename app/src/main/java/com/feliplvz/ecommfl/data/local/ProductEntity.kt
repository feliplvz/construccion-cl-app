package com.feliplvz.ecommfl.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val stock: Int,
    val category: String
)
