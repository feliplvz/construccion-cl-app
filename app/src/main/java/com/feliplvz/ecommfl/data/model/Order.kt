package com.feliplvz.ecommfl.data.model

import com.feliplvz.ecommfl.data.local.OrderEntity

data class Order(
    val id: Long = 0,
    val orderNumber: String,
    val totalAmount: Double,
    val items: String,
    val orderDate: Long,
    val status: OrderStatus
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
    status = OrderStatus.valueOf(status)
)

fun Order.toEntity() = OrderEntity(
    id = id,
    orderNumber = orderNumber,
    totalAmount = totalAmount,
    items = items,
    orderDate = orderDate,
    status = status.name
)

