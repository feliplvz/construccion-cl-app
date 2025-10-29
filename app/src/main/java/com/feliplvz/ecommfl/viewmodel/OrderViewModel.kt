package com.feliplvz.ecommfl.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.feliplvz.ecommfl.data.local.AppDatabase
import com.feliplvz.ecommfl.data.model.Order
import com.feliplvz.ecommfl.data.model.OrderStatus
import com.feliplvz.ecommfl.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OrderRepository

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = OrderRepository(database.orderDao())
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            repository.allOrders.collect { orderList ->
                _orders.value = orderList
            }
        }
    }

    fun createOrder(order: Order) {
        viewModelScope.launch {
            repository.createOrder(order)
        }
    }

    fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
        }
    }
}

