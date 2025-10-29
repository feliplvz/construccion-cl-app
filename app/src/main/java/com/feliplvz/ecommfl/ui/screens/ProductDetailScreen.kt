package com.feliplvz.ecommfl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.feliplvz.ecommfl.data.model.CartItem
import com.feliplvz.ecommfl.data.model.Product
import com.feliplvz.ecommfl.ui.theme.*
import com.feliplvz.ecommfl.viewmodel.CartViewModel
import com.feliplvz.ecommfl.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Long,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onBackClick: () -> Unit
) {
    var product by remember { mutableStateOf<Product?>(null) }
    var quantity by remember { mutableIntStateOf(1) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(productId) {
        product = productViewModel.products.value.find { it.id == productId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { padding ->
        product?.let { prod ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen del producto
                AsyncImage(
                    model = prod.imageUrl,
                    contentDescription = prod.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(BackgroundLight),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Badge de categoría
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PrimaryBlue.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = prod.category,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre
                    Text(
                        text = prod.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stock
                    Text(
                        text = if (prod.stock > 0) "${prod.stock} disponibles" else "Sin stock",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (prod.stock > 0) TextSecondary else ErrorRed,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Descripción
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = prod.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Selector de cantidad
                    if (prod.stock > 0) {
                        Text(
                            text = "Cantidad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Surface(
                                onClick = { if (quantity > 1) quantity-- },
                                shape = RoundedCornerShape(12.dp),
                                color = if (quantity > 1) BackgroundLight else BackgroundLight.copy(alpha = 0.5f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Disminuir",
                                        tint = TextPrimary
                                    )
                                }
                            }

                            Text(
                                text = quantity.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.width(60.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Surface(
                                onClick = { if (quantity < prod.stock) quantity++ },
                                shape = RoundedCornerShape(12.dp),
                                color = if (quantity < prod.stock) PrimaryOrange else PrimaryOrange.copy(alpha = 0.5f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Aumentar",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // Barra inferior flotante con precio y botón
            if (prod.stock > 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 16.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Precio",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "$${String.format(Locale.US, "%.2f", prod.price)}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryOrange
                                )
                            }

                            Button(
                                onClick = {
                                    cartViewModel.addToCart(
                                        CartItem(
                                            productId = prod.id,
                                            productName = prod.name,
                                            productPrice = prod.price,
                                            productImage = prod.imageUrl,
                                            quantity = quantity
                                        )
                                    )
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "$quantity agregado al carrito"
                                        )
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryBlue
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Agregar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

