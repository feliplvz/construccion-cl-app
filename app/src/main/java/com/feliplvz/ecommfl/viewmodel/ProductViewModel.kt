package com.feliplvz.ecommfl.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.feliplvz.ecommfl.data.local.AppDatabase
import com.feliplvz.ecommfl.data.model.Product
import com.feliplvz.ecommfl.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()


    init {
        val database = AppDatabase.getDatabase(application)
        repository = ProductRepository(database.productDao())
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            repository.allProducts.collect { productList ->
                _products.value = productList
            }
        }
    }


    fun insertProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}

