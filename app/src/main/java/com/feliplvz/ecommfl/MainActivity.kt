package com.feliplvz.ecommfl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.feliplvz.ecommfl.data.DatabaseSeeder
import com.feliplvz.ecommfl.data.local.AppDatabase
import com.feliplvz.ecommfl.navigation.AppNavigation
import com.feliplvz.ecommfl.ui.theme.EcommFlTheme
import com.feliplvz.ecommfl.viewmodel.CartViewModel
import com.feliplvz.ecommfl.viewmodel.OrderViewModel
import com.feliplvz.ecommfl.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar bd con datos mock
        val database = AppDatabase.getDatabase(applicationContext)
        DatabaseSeeder.seedDatabase(database)

        enableEdgeToEdge()
        setContent {
            EcommFlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val productViewModel: ProductViewModel = viewModel()
                    val cartViewModel: CartViewModel = viewModel()
                    val orderViewModel: OrderViewModel = viewModel()
                    val authViewModel: com.feliplvz.ecommfl.viewmodel.AuthViewModel = viewModel()

                    AppNavigation(
                        navController = navController,
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel,
                        orderViewModel = orderViewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

