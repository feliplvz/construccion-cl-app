package com.feliplvz.ecommfl.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    fun getOrdersByUserId(userId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE userId IS NULL ORDER BY orderDate DESC")
    fun getGuestOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: String)
}

