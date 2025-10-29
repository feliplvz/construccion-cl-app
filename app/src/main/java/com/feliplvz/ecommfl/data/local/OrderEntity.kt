package com.feliplvz.ecommfl.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val totalAmount: Double,
    val items: String,
    val orderDate: Long,
    val status: String,
    val customerName: String? = null,
    val customerPhone: String? = null,
    val customerAddress: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationAddress: String? = null
)

