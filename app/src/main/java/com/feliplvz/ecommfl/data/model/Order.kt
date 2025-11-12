package com.feliplvz.ecommfl.data.model

import com.feliplvz.ecommfl.data.local.OrderEntity

data class Order(
    val id: Long = 0,
    val orderNumber: String,
    val totalAmount: Double,
    val items: String,
    val orderDate: Long,
    val status: OrderStatus,
    val customerName: String? = null,
    val customerPhone: String? = null,
    val customerAddress: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationAddress: String? = null,
    val userId: String? = null
)

enum class OrderStatus {
    PENDIENTE,
    COMPLETADO,
    CANCELADO
}

fun OrderEntity.toOrder() = Order(
    id = id,
    orderNumber = orderNumber,
    totalAmount = totalAmount,
    items = items,
    orderDate = orderDate,
    status = OrderStatus.valueOf(status),
    customerName = customerName,
    customerPhone = customerPhone,
    customerAddress = customerAddress,
    latitude = latitude,
    longitude = longitude,
    locationAddress = locationAddress,
    userId = userId
)

fun Order.toEntity() = OrderEntity(
    id = id,
    orderNumber = orderNumber,
    totalAmount = totalAmount,
    items = items,
    orderDate = orderDate,
    status = status.name,
    customerName = customerName,
    customerPhone = customerPhone,
    customerAddress = customerAddress,
    latitude = latitude,
    longitude = longitude,
    locationAddress = locationAddress,
    userId = userId
)

