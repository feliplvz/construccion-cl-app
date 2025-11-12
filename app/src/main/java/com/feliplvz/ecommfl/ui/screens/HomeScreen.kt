package com.feliplvz.ecommfl.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feliplvz.ecommfl.ui.theme.*
import com.feliplvz.ecommfl.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showContent = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CONSTRUCCION.CL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        letterSpacing = (-0.5).sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimary
                ),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = TextPrimary
                        )
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
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Banner minimalista
            PromoBanner(showContent)

            Spacer(modifier = Modifier.height(32.dp))

            // Grid de opciones principales - Estilo minimalista
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        icon = Icons.Default.Build,
                        title = "Productos",
                        subtitle = "Catálogo",
                        color = PrimaryBlue,
                        delay = 100,
                        visible = showContent,
                        onClick = onNavigateToProducts,
                        modifier = Modifier.weight(1f)
                    )

                    CategoryCard(
                        icon = Icons.Default.ShoppingCart,
                        title = "Carrito",
                        subtitle = "Ver items",
                        color = PrimaryOrange,
                        delay = 200,
                        visible = showContent,
                        onClick = onNavigateToCart,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        icon = Icons.Default.Notifications,
                        title = "Pedidos",
                        subtitle = "Historial",
                        color = AccentOrange,
                        delay = 300,
                        visible = showContent,
                        onClick = onNavigateToOrders,
                        modifier = Modifier.weight(1f)
                    )

                    CategoryCard(
                        icon = if (authState.isAuthenticated) Icons.Default.ExitToApp else Icons.Default.AccountCircle,
                        title = if (authState.isAuthenticated) "Salir" else "Mi Cuenta",
                        subtitle = if (authState.isAuthenticated) "Cerrar sesión" else "Ingresar",
                        color = PrimaryBlue,
                        delay = 400,
                        visible = showContent,
                        onClick = {
                            if (authState.isAuthenticated) {
                                authViewModel.signOut()
                            } else {
                                onNavigateToLogin()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Solo mostrar Admin si el usuario es administrador
                if (authViewModel.isAdmin()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CategoryCard(
                            icon = Icons.Default.Person,
                            title = "Admin",
                            subtitle = "Gestión",
                            color = SecondaryTeal,
                            delay = 500,
                            visible = showContent,
                            onClick = onNavigateToAdmin,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Categorías de ferretería
            CategorySection(showContent)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PromoBanner(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = PrimaryBlue
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ofertas de la semana",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hasta 30% de descuento",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Icon(
                    Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = PrimaryOrange
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    delay: Int,
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300, delayMillis = delay)) +
                slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(300, delayMillis = delay)
                ),
        modifier = modifier
    ) {
        Card(
            onClick = {
                scale = 0.95f
                scale = 1f
                onClick()
            },
            modifier = Modifier
                .height(130.dp)
                .scale(animatedScale),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundLight
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = color
                )

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySection(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300, delayMillis = 500))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CategoryChip(
                    text = "Herramientas",
                    icon = Icons.Default.Build,
                    modifier = Modifier.weight(1f)
                )
                CategoryChip(
                    text = "Electricidad",
                    icon = Icons.Default.Star,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CategoryChip(
                    text = "Pintura",
                    icon = Icons.Default.Create,
                    modifier = Modifier.weight(1f)
                )
                CategoryChip(
                    text = "Plomería",
                    icon = Icons.Default.Settings,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = TextSecondary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}