package com.feliplvz.ecommfl.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.feliplvz.ecommfl.data.local.AppDatabase
import com.feliplvz.ecommfl.data.model.CartItem
import com.feliplvz.ecommfl.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CartRepository

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _cartTotal = MutableStateFlow(0.0)
    val cartTotal: StateFlow<Double> = _cartTotal.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = CartRepository(database.cartDao())
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            repository.allCartItems.collect { items ->
                _cartItems.value = items
            }
        }
        viewModelScope.launch {
            repository.cartTotal.collect { total ->
                _cartTotal.value = total
            }
        }
    }

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            repository.addToCart(cartItem)
        }
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity > 0) {
                repository.updateCartItem(cartItem.copy(quantity = newQuantity))
            } else {
                repository.removeFromCart(cartItem)
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            repository.removeFromCart(cartItem)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}

