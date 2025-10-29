package com.feliplvz.ecommfl.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.feliplvz.ecommfl.data.model.Product
import com.feliplvz.ecommfl.utils.FormValidator
import com.feliplvz.ecommfl.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    productViewModel: ProductViewModel,
    onBackClick: () -> Unit,
    onProductAdded: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Herramientas", "Electricidad", "Pintura", "Plomería", "Construcción", "Jardín")
    var showCategoryMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Información del Producto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    val validation = FormValidator.validateProductName(it)
                    nameError = validation.errorMessage
                },
                label = { Text("Nombre *") },
                leadingIcon = { Icon(Icons.Default.ShoppingCart, null) },
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Descripción
            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    val validation = FormValidator.validateDescription(it)
                    descriptionError = validation.errorMessage
                },
                label = { Text("Descripción *") },
                leadingIcon = { Icon(Icons.Default.Info, null) },
                isError = descriptionError != null,
                supportingText = descriptionError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Campo Precio
            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                    val validation = FormValidator.validatePrice(it)
                    priceError = validation.errorMessage
                },
                label = { Text("Precio *") },
                leadingIcon = { Icon(Icons.Default.Star, null) },
                isError = priceError != null,
                supportingText = priceError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Stock
            OutlinedTextField(
                value = stock,
                onValueChange = {
                    stock = it
                    val validation = FormValidator.validateStock(it)
                    stockError = validation.errorMessage
                },
                label = { Text("Stock *") },
                leadingIcon = { Icon(Icons.Default.Info, null) },
                isError = stockError != null,
                supportingText = stockError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Categoría (Dropdown)
            ExposedDropdownMenuBox(
                expanded = showCategoryMenu,
                onExpandedChange = { showCategoryMenu = it }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría *") },
                    leadingIcon = { Icon(Icons.Default.MoreVert, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu) },
                    isError = categoryError != null,
                    supportingText = categoryError?.let { { Text(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { showCategoryMenu = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                categoryError = null
                                showCategoryMenu = false
                            }
                        )
                    }
                }
            }

            // Campo URL de imagen
            OutlinedTextField(
                value = imageUrl,
                onValueChange = {
                    imageUrl = it
                    val validation = FormValidator.validateImageUrl(it)
                    imageError = validation.errorMessage
                },
                label = { Text("URL de Imagen *") },
                leadingIcon = { Icon(Icons.Default.Place, null) },
                isError = imageError != null,
                supportingText = imageError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            // Botón para tomar foto
            OutlinedButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Función de cámara disponible - Permiso integrado")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Build, null)
                Spacer(Modifier.width(8.dp))
                Text("Tomar Foto con Cámara")
            }

            // Preview de imagen
            if (imageUrl.isNotBlank() && imageError == null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Guardar
            Button(
                onClick = {
                    val validations = FormValidator.validateProduct(
                        name, description, price, stock, category, imageUrl
                    )

                    nameError = validations["name"]?.errorMessage
                    descriptionError = validations["description"]?.errorMessage
                    priceError = validations["price"]?.errorMessage
                    stockError = validations["stock"]?.errorMessage
                    categoryError = validations["category"]?.errorMessage
                    imageError = validations["imageUrl"]?.errorMessage

                    if (FormValidator.isFormValid(validations)) {
                        val product = Product(
                            name = name,
                            description = description,
                            price = price.toDouble(),
                            stock = stock.toInt(),
                            category = category,
                            imageUrl = imageUrl
                        )

                        productViewModel.insertProduct(product)
                        onProductAdded()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor corrige los errores")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar Producto", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

