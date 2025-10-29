package com.feliplvz.ecommfl.data.repository

import com.feliplvz.ecommfl.data.local.OrderDao
import com.feliplvz.ecommfl.data.model.Order
import com.feliplvz.ecommfl.data.model.OrderStatus
import com.feliplvz.ecommfl.data.model.toEntity
import com.feliplvz.ecommfl.data.model.toOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(private val orderDao: OrderDao) {

    val allOrders: Flow<List<Order>> =
        orderDao.getAllOrders().map { entities ->
            entities.map { it.toOrder() }
        }

    suspend fun createOrder(order: Order): Long {
        return orderDao.insertOrder(order.toEntity())
    }

    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        orderDao.updateOrderStatus(orderId, status.name)
    }
}

