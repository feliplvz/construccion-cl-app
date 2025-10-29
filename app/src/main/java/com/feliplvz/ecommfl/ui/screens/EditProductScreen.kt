package com.feliplvz.ecommfl.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feliplvz.ecommfl.data.model.Product
import com.feliplvz.ecommfl.utils.FormValidator
import com.feliplvz.ecommfl.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: Long,
    productViewModel: ProductViewModel,
    onBackClick: () -> Unit,
    onProductUpdated: () -> Unit
) {
    var product by remember { mutableStateOf<Product?>(null) }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(productId) {
        product = productViewModel.products.value.find { it.id == productId }
        product?.let {
            name = it.name
            description = it.description
            price = it.price.toString()
            stock = it.stock.toString()
            category = it.category
            imageUrl = it.imageUrl
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = FormValidator.validateProductName(it).errorMessage
                },
                label = { Text("Nombre") },
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                    priceError = FormValidator.validatePrice(it).errorMessage
                },
                label = { Text("Precio") },
                isError = priceError != null,
                supportingText = priceError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = stock,
                onValueChange = {
                    stock = it
                    stockError = FormValidator.validateStock(it).errorMessage
                },
                label = { Text("Stock") },
                isError = stockError != null,
                supportingText = stockError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de Imagen") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nameError == null && priceError == null && stockError == null) {
                        product?.let {
                            val updatedProduct = it.copy(
                                name = name,
                                description = description,
                                price = price.toDouble(),
                                stock = stock.toInt(),
                                category = category,
                                imageUrl = imageUrl
                            )
                            productViewModel.updateProduct(updatedProduct)
                            onProductUpdated()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Actualizar Producto", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

