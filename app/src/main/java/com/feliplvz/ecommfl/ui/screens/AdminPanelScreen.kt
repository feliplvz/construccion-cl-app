package com.feliplvz.ecommfl.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        IconButton(onClick = { onDelete(product) }) {
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
}

@Composable
fun AdminOrdersList(
    orders: List<com.feliplvz.ecommfl.data.model.Order>,
    onUpdateStatus: (Long, com.feliplvz.ecommfl.data.model.OrderStatus) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders) { order ->
            Card(
                modifier = Modifier.fillMaxWidth()
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
                        Text(
                            text = order.orderNumber,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", order.totalAmount)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Estado: ${order.status.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (order.status == com.feliplvz.ecommfl.data.model.OrderStatus.PENDIENTE) {
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
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}

