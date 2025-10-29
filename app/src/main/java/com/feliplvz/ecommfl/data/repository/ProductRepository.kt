package com.feliplvz.ecommfl.data.repository

import com.feliplvz.ecommfl.data.local.ProductDao
import com.feliplvz.ecommfl.data.model.Product
import com.feliplvz.ecommfl.data.model.toEntity
import com.feliplvz.ecommfl.data.model.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> =
        productDao.getAllProducts().map { entities ->
            entities.map { it.toProduct() }
        }

    suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product.toEntity())
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product.toEntity())
    }
}

