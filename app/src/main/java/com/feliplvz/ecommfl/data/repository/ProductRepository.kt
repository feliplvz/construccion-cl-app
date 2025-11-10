package com.feliplvz.ecommfl.data.repository

import com.feliplvz.ecommfl.data.model.Product
import com.feliplvz.ecommfl.data.network.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository {
    private val supabase = SupabaseClient.client

    val allProducts: Flow<List<Product>> = flow {
        try {
            val products = supabase.from("products")
                .select()
                .decodeList<Product>()
            emit(products)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    suspend fun insertProduct(product: Product): Long {
        return try {
            supabase.from("products").insert(product)
            0L
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateProduct(product: Product) {
        try {
            supabase.from("products")
                .update(product) {
                    filter {
                        eq("id", product.id)
                    }
                }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteProduct(product: Product) {
        try {
            supabase.from("products")
                .delete {
                    filter {
                        eq("id", product.id)
                    }
                }
        } catch (e: Exception) {
            throw e
        }
    }
}

