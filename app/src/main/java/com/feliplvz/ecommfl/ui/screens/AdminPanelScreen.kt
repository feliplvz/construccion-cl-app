package com.feliplvz.ecommfl.ui.screens

import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import com.feliplvz.ecommfl.data.model.Product
import com.feliplvz.ecommfl.ui.theme.*
import com.feliplvz.ecommfl.viewmodel.OrderViewModel
import com.feliplvz.ecommfl.viewmodel.ProductViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    productViewModel: ProductViewModel,
    orderViewModel: OrderViewModel,
    onAddProductClick: () -> Unit,
    onEditProductClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val products by productViewModel.products.collectAsState()
    val orders by orderViewModel.orders.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Panel Admin",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
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
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = onAddProductClick,
                    containerColor = PrimaryOrange,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, "Agregar producto")
                }
            }
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Productos (${products.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Pedidos (${orders.size})") }
                )
            }

            when (selectedTab) {
                0 -> AdminProductsList(
                    products = products,
                    onEdit = onEditProductClick,
                    onDelete = { productViewModel.deleteProduct(it) }
                )
                1 -> AdminOrdersList(
                    orders = orders,
                    onUpdateStatus = { orderId, status ->
                        orderViewModel.updateOrderStatus(orderId, status)
                    }
                )
            }
        }
    }
}

@Composable
fun AdminProductsList(
    products: List<Product>,
    onEdit: (Long) -> Unit,
    onDelete: (Product) -> Unit
) {
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${product.price} | Stock: ${product.stock}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row {
                        IconButton(onClick = { onEdit(product.id) }) {
                            Icon(Icons.Default.Edit, "Editar")
                        }
                        IconButton(onClick = { productToDelete = product }) {
                            Icon(
                                Icons.Default.Delete,
                                "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación

    productToDelete?.let { product ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            },
            title = {
                Text(
                    text = "Eliminar Producto",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar el producto \"${product.name}\"?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(product)
                        productToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
            }
}

@Composable
fun AdminOrdersList(
    orders: List<com.feliplvz.ecommfl.data.model.Order>,
    onUpdateStatus: (Long, com.feliplvz.ecommfl.data.model.OrderStatus) -> Unit
) {
    var selectedOrder by remember { mutableStateOf<com.feliplvz.ecommfl.data.model.Order?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders) { order ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { selectedOrder = order }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = order.orderNumber,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (order.customerName != null) {
                                Text(
                                    text = order.customerName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", order.totalAmount)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (order.status) {
                                com.feliplvz.ecommfl.data.model.OrderStatus.PENDIENTE ->
                                    MaterialTheme.colorScheme.tertiary
                                com.feliplvz.ecommfl.data.model.OrderStatus.COMPLETADO ->
                                    MaterialTheme.colorScheme.primary
                                com.feliplvz.ecommfl.data.model.OrderStatus.CANCELADO ->
                                    MaterialTheme.colorScheme.error
                            }
                        ) {
                            Text(
                                text = order.status.name,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }

                        TextButton(onClick = { selectedOrder = order }) {
                            Text("Ver Detalle")
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    if (order.status == com.feliplvz.ecommfl.data.model.OrderStatus.PENDIENTE) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    onUpdateStatus(
                                        order.id,
                                        com.feliplvz.ecommfl.data.model.OrderStatus.COMPLETADO
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Completar")
                            }
                            OutlinedButton(
                                onClick = {
                                    onUpdateStatus(
                                        order.id,
                                        com.feliplvz.ecommfl.data.model.OrderStatus.CANCELADO
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de detalle del pedido
    selectedOrder?.let { order ->
        OrderDetailDialog(
            order = order,
            onDismiss = { selectedOrder = null },
            onUpdateStatus = { status ->
                onUpdateStatus(order.id, status)
                selectedOrder = null
            }
        )
    }
}

@Composable
fun OrderDetailDialog(
    order: com.feliplvz.ecommfl.data.model.Order,
    onDismiss: () -> Unit,
    onUpdateStatus: (com.feliplvz.ecommfl.data.model.OrderStatus) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    var addressFromGPS by remember { mutableStateOf<String?>(null) }

    // Obtener dirección desde GPS
    LaunchedEffect(order.latitude, order.longitude) {
        if (order.latitude != null && order.longitude != null) {
            scope.launch {
                addressFromGPS = getAddressFromCoordinatesAdmin(
                    context,
                    order.latitude,
                    order.longitude
                )
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Detalle del Pedido",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = order.orderNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, "Cerrar")
                        }
                    }
                }

                // Contenido scrolleable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Estado
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (order.status) {
                            com.feliplvz.ecommfl.data.model.OrderStatus.PENDIENTE ->
                                MaterialTheme.colorScheme.tertiary
                            com.feliplvz.ecommfl.data.model.OrderStatus.COMPLETADO ->
                                MaterialTheme.colorScheme.primary
                            com.feliplvz.ecommfl.data.model.OrderStatus.CANCELADO ->
                                MaterialTheme.colorScheme.error
                        }
                    ) {
                        Text(
                            text = "Estado: ${order.status.name}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Fecha
                    InfoSection(
                        title = "Fecha del pedido",
                        content = dateFormat.format(Date(order.orderDate))
                    )

                    HorizontalDivider()

                    // Información del cliente
                    if (order.customerName != null || order.customerPhone != null || order.customerAddress != null) {
                        Text(
                            text = "Información del Cliente",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        order.customerName?.let {
                            InfoRow(icon = Icons.Default.Person, label = "Nombre", value = it)
                        }
                        order.customerPhone?.let {
                            InfoRow(icon = Icons.Default.Phone, label = "Teléfono", value = it)
                        }
                        order.customerAddress?.let {
                            InfoRow(icon = Icons.Default.Home, label = "Dirección", value = it)
                        }

                        HorizontalDivider()
                    }

                    // Items del pedido
                    Text(
                        text = "Items del Pedido",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            order.items.split(";").forEach { item ->
                                val parts = item.split(":")
                                if (parts.size == 2) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "• ${parts[0]}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "x${parts[1]}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider()

                    // Ubicación GPS
                    if (order.latitude != null && order.longitude != null) {
                        Text(
                            text = "Ubicación GPS del Pedido",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📍",
                                        fontSize = 20.sp
                                    )
                                    Column {
                                        if (addressFromGPS != null) {
                                            Text(
                                                text = addressFromGPS!!,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium
                                            )
                                        } else {
                                            Text(
                                                text = "Obteniendo dirección...",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                            )
                                        }
                                        Text(
                                            text = "Coordenadas: ${String.format(Locale.US, "%.4f", order.latitude)}, ${String.format(Locale.US, "%.4f", order.longitude)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider()
                    }

                    // Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total del Pedido:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", order.totalAmount)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Footer con acciones
                if (order.status == com.feliplvz.ecommfl.data.model.OrderStatus.PENDIENTE) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { onUpdateStatus(com.feliplvz.ecommfl.data.model.OrderStatus.COMPLETADO) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Completar")
                            }
                            OutlinedButton(
                                onClick = { onUpdateStatus(com.feliplvz.ecommfl.data.model.OrderStatus.CANCELADO) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoSection(title: String, content: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Función para obtener dirección desde GPS (admin)
@Suppress("DEPRECATION")
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
suspend fun getAddressFromCoordinatesAdmin(
    context: android.content.Context,
    latitude: Double,
    longitude: Double
): String? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    val address = addresses.firstOrNull()
                    val addressText = if (address != null) {
                        buildString {
                            address.thoroughfare?.let { append("$it ") }
                            address.subThoroughfare?.let { append("$it, ") }
                            address.locality?.let { append("$it, ") }
                            address.adminArea?.let { append(it) }
                        }.trim().ifEmpty { "Dirección no disponible" }
                    } else {
                        "Dirección no disponible"
                    }
                    continuation.resume(addressText, null)
                }
            }
        } else {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses?.firstOrNull()
            if (address != null) {
                buildString {
                    address.thoroughfare?.let { append("$it ") }
                    address.subThoroughfare?.let { append("$it, ") }
                    address.locality?.let { append("$it, ") }
                    address.adminArea?.let { append(it) }
                }.trim().ifEmpty { "Dirección no disponible" }
            } else {
                "Dirección no disponible"
            }
        }
    } catch (_: Exception) {
        null
    }
}
