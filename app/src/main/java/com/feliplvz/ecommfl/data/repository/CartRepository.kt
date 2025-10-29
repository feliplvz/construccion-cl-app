package com.feliplvz.ecommfl.data.repository

import com.feliplvz.ecommfl.data.local.CartDao
import com.feliplvz.ecommfl.data.model.CartItem
import com.feliplvz.ecommfl.data.model.toCartItem
import com.feliplvz.ecommfl.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(private val cartDao: CartDao) {

    val allCartItems: Flow<List<CartItem>> =
        cartDao.getAllCartItems().map { entities ->
            entities.map { it.toCartItem() }
        }

    val cartTotal: Flow<Double> =
        cartDao.getCartTotal().map { it ?: 0.0 }

    suspend fun addToCart(cartItem: CartItem) {
        val existingItem = cartDao.getCartItemByProductId(cartItem.productId)
        if (existingItem != null) {
            // Si ya existe, actualizar cantidad
            val updated = existingItem.copy(
                quantity = existingItem.quantity + cartItem.quantity
            )
            cartDao.updateCartItem(updated)
        } else {
            // Si no existe, insertar nuevo
            cartDao.insertCartItem(cartItem.toEntity())
        }
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(cartItem.toEntity())
    }

    suspend fun removeFromCart(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem.toEntity())
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}

