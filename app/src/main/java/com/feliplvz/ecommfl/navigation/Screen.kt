package com.feliplvz.ecommfl.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ProductList : Screen("product_list")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Long) = "product_detail/$productId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object OrderHistory : Screen("order_history")
    object AdminPanel : Screen("admin_panel")
    object AddProduct : Screen("add_product")
    object EditProduct : Screen("edit_product/{productId}") {
        fun createRoute(productId: Long) = "edit_product/$productId"
    }
    object Login : Screen("login")
    object AdminLogin : Screen("admin_login")
    object Register : Screen("register")
}

