package com.feliplvz.ecommfl.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feliplvz.ecommfl.data.model.Order
import com.feliplvz.ecommfl.data.model.OrderStatus
import com.feliplvz.ecommfl.viewmodel.CartViewModel
import com.feliplvz.ecommfl.viewmodel.OrderViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    onBackClick: () -> Unit,
    onOrderComplete: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.cartTotal.collectAsState()

    var customerName by remember { mutableStateOf("") }
    var customerAddress by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<Location?>(null) }
    var locationAddress by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Permisos de ubicación
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Información de Entrega",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = customerPhone,
                onValueChange = { customerPhone = it },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = customerAddress,
                onValueChange = { customerAddress = it },
                label = { Text("Dirección de entrega") },
                leadingIcon = { Icon(Icons.Default.Home, null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            // Botón para obtener ubicación GPS
            OutlinedButton(
                onClick = {
                    if (locationPermissions.allPermissionsGranted) {
                        scope.launch {
                            isLoading = true
                            val loc = getLocation(context)
                            location = loc
                            // Obtener dirección legible
                            if (loc != null) {
                                locationAddress = getAddressFromLocation(context, loc.latitude, loc.longitude)
                            }
                            isLoading = false
                        }
                    } else {
                        locationPermissions.launchMultiplePermissionRequest()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.LocationOn, null)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (locationPermissions.allPermissionsGranted) {
                        "Capturar Ubicación GPS"
                    } else {
                        "Dar Permiso de Ubicación"
                    }
                )
            }

            // Mostrar ubicación capturada
            if (location != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "✓ Ubicación GPS capturada",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            if (locationAddress != null) {
                                Text(
                                    "📍 $locationAddress",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Text(
                                    "Obteniendo dirección...",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                            Text(
                                "Coordenadas: ${String.format(Locale.US, "%.4f", location?.latitude)}, ${String.format(Locale.US, "%.4f", location?.longitude)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            Text(
                text = "Resumen del Pedido",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            cartItems.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.quantity}x ${item.productName}")
                    Text("$${String.format(Locale.US, "%.2f", item.productPrice * item.quantity)}")
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$${String.format(Locale.US, "%.2f", cartTotal)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true

                        val orderNumber = "ORD-${System.currentTimeMillis()}"
                        val itemsJson = cartItems.joinToString(";") {
                            "${it.productName}:${it.quantity}"
                        }

                        val order = Order(
                            orderNumber = orderNumber,
                            totalAmount = cartTotal,
                            items = itemsJson,
                            orderDate = System.currentTimeMillis(),
                            status = OrderStatus.PENDIENTE,
                            customerName = customerName,
                            customerPhone = customerPhone,
                            customerAddress = customerAddress,
                            latitude = location?.latitude,
                            longitude = location?.longitude
                        )

                        orderViewModel.createOrder(order)
                        cartViewModel.clearCart()

                        isLoading = false
                        onOrderComplete()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = customerName.isNotBlank() &&
                         customerAddress.isNotBlank() &&
                         customerPhone.isNotBlank() &&
                         !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Confirmar Pedido", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("MissingPermission")
suspend fun getLocation(context: Context): Location? {
    return try {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    continuation.resume(location, null)
                }
                .addOnFailureListener {
                    continuation.resume(null, null)
                }
        }
    } catch (_: Exception) {
        null
    }
}

// Función para obtener dirección legible desde coordenadas GPS
@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun getAddressFromLocation(
    context: Context,
    latitude: Double,
    longitude: Double
): String? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13+ usar el API nuevo
            kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    val address = addresses.firstOrNull()
                    val addressText = if (address != null) {
                        buildString {
                            address.thoroughfare?.let { append("$it ") }
                            address.subThoroughfare?.let { append("$it, ") }
                            address.locality?.let { append("$it, ") }
                            address.adminArea?.let { append(it) }
                        }.trim().ifEmpty { "Dirección obtenida" }
                    } else {
                        "Dirección obtenida"
                    }
                    continuation.resume(addressText, null)
                }
            }
        } else {
            // Para versiones antiguas
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses?.firstOrNull()
            if (address != null) {
                buildString {
                    address.thoroughfare?.let { append("$it ") }
                    address.subThoroughfare?.let { append("$it, ") }
                    address.locality?.let { append("$it, ") }
                    address.adminArea?.let { append(it) }
                }.trim().ifEmpty { "Dirección obtenida" }
            } else {
                "Dirección obtenida"
            }
        }
    } catch (_: Exception) {
        null
    }
}
