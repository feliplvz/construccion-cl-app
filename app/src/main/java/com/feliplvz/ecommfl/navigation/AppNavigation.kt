package com.feliplvz.ecommfl.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.feliplvz.ecommfl.ui.screens.*
import com.feliplvz.ecommfl.viewmodel.CartViewModel
import com.feliplvz.ecommfl.viewmodel.OrderViewModel
import com.feliplvz.ecommfl.viewmodel.ProductViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Pantalla principal (Home)
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProducts = { navController.navigate(Screen.ProductList.route) },
                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                onNavigateToOrders = { navController.navigate(Screen.OrderHistory.route) },
                onNavigateToAdmin = { navController.navigate(Screen.AdminPanel.route) }
            )
        }

        // Lista de productos
        composable(Screen.ProductList.route) {
            ProductListScreen(
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onCartClick = { navController.navigate(Screen.Cart.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Detalle de producto
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            ProductDetailScreen(
                productId = productId,
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Carrito
        composable(Screen.Cart.route) {
            CartScreen(
                cartViewModel = cartViewModel,
                onBackClick = { navController.popBackStack() },
                onCheckoutClick = { navController.navigate(Screen.Checkout.route) }
            )
        }

        // Checkout
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                cartViewModel = cartViewModel,
                orderViewModel = orderViewModel,
                onBackClick = { navController.popBackStack() },
                onOrderComplete = {
                    navController.navigate(Screen.OrderHistory.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        // Historial de pedidos
        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(
                orderViewModel = orderViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Panel de administración
        composable(Screen.AdminPanel.route) {
            AdminPanelScreen(
                productViewModel = productViewModel,
                orderViewModel = orderViewModel,
                onAddProductClick = { navController.navigate(Screen.AddProduct.route) },
                onEditProductClick = { productId ->
                    navController.navigate(Screen.EditProduct.createRoute(productId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Agregar producto
        composable(Screen.AddProduct.route) {
            AddProductScreen(
                productViewModel = productViewModel,
                onBackClick = { navController.popBackStack() },
                onProductAdded = { navController.popBackStack() }
            )
        }

        // Editar producto
        composable(
            route = Screen.EditProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            EditProductScreen(
                productId = productId,
                productViewModel = productViewModel,
                onBackClick = { navController.popBackStack() },
                onProductUpdated = { navController.popBackStack() }
            )
        }
    }
}

