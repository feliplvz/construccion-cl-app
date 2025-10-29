package com.feliplvz.ecommfl.data

import com.feliplvz.ecommfl.data.local.AppDatabase
import com.feliplvz.ecommfl.data.local.ProductEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseSeeder {

    fun seedDatabase(database: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            // Verificar si ya hay productos
            database.productDao().getAllProducts().collect { products ->
                if (products.isEmpty()) {
                    // Productos de ejemplo para poblar la bd
                    val sampleProducts = listOf(
                        ProductEntity(
                            name = "Taladro Percutor 650W",
                            description = "Taladro eléctrico con función percutora, ideal para concreto y mampostería. Incluye maletín y accesorios.",
                            price = 89.99,
                            imageUrl = "https://images.unsplash.com/photo-1504148455328-c376907d081c?w=400",
                            stock = 25,
                            category = "Herramientas"
                        ),
                        ProductEntity(
                            name = "Juego Destornilladores 12 Pzas",
                            description = "Set profesional de destornilladores con puntas magnéticas y mango ergonómico antideslizante.",
                            price = 24.99,
                            imageUrl = "https://images.unsplash.com/photo-1530124566582-a618bc2615dc?w=400",
                            stock = 50,
                            category = "Herramientas"
                        ),
                        ProductEntity(
                            name = "Pintura Látex Blanca 4L",
                            description = "Pintura látex de alta calidad, acabado mate lavable. Rendimiento: 40-50 m² por galón.",
                            price = 32.99,
                            imageUrl = "https://images.unsplash.com/photo-1589939705384-5185137a7f0f?w=400",
                            stock = 100,
                            category = "Pintura"
                        ),
                        ProductEntity(
                            name = "Llave Inglesa Ajustable 12\"",
                            description = "Llave ajustable profesional de acero forjado con acabado cromado. Apertura máxima 30mm.",
                            price = 18.50,
                            imageUrl = "https://images.unsplash.com/photo-1581092160562-40aa08e78837?w=400",
                            stock = 40,
                            category = "Herramientas"
                        ),
                        ProductEntity(
                            name = "Cable Eléctrico 2x2.5mm 100m",
                            description = "Cable THW calibre 12 AWG para instalaciones eléctricas residenciales. Certificado.",
                            price = 125.00,
                            imageUrl = "https://images.unsplash.com/photo-1621905252507-b35492cc74b4?w=400",
                            stock = 15,
                            category = "Electricidad"
                        ),
                        ProductEntity(
                            name = "Cinta Métrica 5m Stanley",
                            description = "Flexómetro profesional con cinta de acero templado y freno automático. Precisión garantizada.",
                            price = 12.99,
                            imageUrl = "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?w=400",
                            stock = 60,
                            category = "Herramientas"
                        ),
                        ProductEntity(
                            name = "Llave de Tubo Multiuso",
                            description = "Llave inglesa para plomería con mordazas dentadas. Apertura ajustable hasta 32mm.",
                            price = 15.99,
                            imageUrl = "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=400",
                            stock = 35,
                            category = "Plomería"
                        ),
                        ProductEntity(
                            name = "Brocha Profesional 3\"",
                            description = "Brocha de cerdas sintéticas para pintura látex y esmalte. Mango de madera barnizada.",
                            price = 8.99,
                            imageUrl = "https://images.unsplash.com/photo-1562259949-e8e7689d7828?w=400",
                            stock = 80,
                            category = "Pintura"
                        ),
                        ProductEntity(
                            name = "Interruptor Simple 15A",
                            description = "Interruptor eléctrico de uso general con placa incluida. Certificación UL.",
                            price = 3.50,
                            imageUrl = "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400",
                            stock = 200,
                            category = "Electricidad"
                        ),
                        ProductEntity(
                            name = "Martillo Carpintero 450g",
                            description = "Martillo de acero forjado con mango de fibra de vidrio. Cabeza pulida y balanceada.",
                            price = 16.99,
                            imageUrl = "https://images.unsplash.com/photo-1581092162384-8987c1d64718?w=400",
                            stock = 45,
                            category = "Herramientas"
                        )
                    )

                    sampleProducts.forEach { product ->
                        database.productDao().insertProduct(product)
                    }
                }
            }
        }
    }
}

