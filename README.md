# CONSTRUCCION.CL

App móvil de e-commerce para productos de ferretería y construcción, desarrollada en Android con Kotlin y Jetpack Compose.

## Equipo

- **Felipe López** - Dev

**DSY1105** - Desarrollo de Aplicaciones Móviles
**Fecha:** Octubre 2025

---

## Sobre el proyecto

Esta aplicación es un sistema de comercio electrónico enfocado en productos de ferretería. Permite a los usuarios explorar un catálogo de productos, gestionar un carrito de compras y realizar pedidos. También incluye un panel de administración para la gestión de inventario.

El proyecto se desarrolló como parte de la EV2 de la asignatura. La idea surge de mi experiencia en un ecommerce que actualmente opera en VTEX, por lo que quise aplicar ese conocimiento en una solución móvil.

---

## Funcionalidades principales

### Para usuarios (clientes)
- Navegación del catálogo de productos
- Vista detallada de cada producto con información de precio y stock
- Sistema de carrito de compras con gestión de cantidades
- Proceso de checkout con formulario de datos de envío
- Captura de ubicación GPS al realizar pedidos
- Historial completo de pedidos realizados

### Para administradores
- Panel de gestión de productos con operaciones CRUD
- Captura de fotos usando la cámara del dispositivo para nuevos productos
- Actualización de inventario y precios
- Eliminación de productos

### Recursos del dispositivo integrados
- **Cámara:** Para capturar fotos de productos al agregarlos al inventario
- **GPS:** Para registrar la ubicación del cliente al momento de realizar el pedido

---

## Arquitectura técnica

El proyecto sigue el patrón MVVM (Model-View-ViewModel) con una clara separación de responsabilidades:

### Capas de la aplicación
```
UI Layer (Composables) → ViewModel → Repository → DAO → Room Database
```

**Tecnologías utilizadas:**
- Jetpack Compose para la interfaz de usuario
- Room Database para persistencia local
- Kotlin Coroutines y Flow para operaciones asíncronas
- Navigation Component para la navegación entre pantallas
- Material Design 3 para el sistema de diseño

**Patrones implementados:**
- MVVM para la separación de lógica de negocio y UI
- Repository Pattern para abstracción de la fuente de datos
- DAO Pattern para acceso a la base de datos

---

## Estructura del código

```
app/src/main/java/com/feliplvz/ecommfl/
├── data/
│   ├── local/
│   │   ├── dao/              (Data Access Objects)
│   │   ├── entity/           (Entidades de Room)
│   │   └── AppDatabase.kt
│   ├── model/                (Modelos de dominio)
│   └── repository/           (Capa de abstracción de datos)
├── navigation/               (Configuración de rutas)
├── ui/
│   ├── screens/              (Pantallas de la app)
│   └── theme/                (Colores, tipografía, tema)
├── utils/                    (Utilidades, validaciones)
├── viewmodel/                (Lógica de negocio)
└── MainActivity.kt
```

**Pantallas implementadas:**
- HomeScreen: Pantalla principal con accesos rápidos
- ProductListScreen: Catálogo de productos en grid
- ProductDetailScreen: Detalle individual de producto
- CartScreen: Visualización y gestión del carrito
- CheckoutScreen: Formulario de finalización de compra
- OrderHistoryScreen: Historial de pedidos
- AdminPanelScreen: Panel de administración
- AddProductScreen: Formulario para agregar productos
- EditProductScreen: Formulario de edición de productos

---

## Base de datos

La aplicación usa Room para persistir tres tipos de datos:

**Tabla Products**
Almacena el catálogo completo de productos con nombre, descripción, precio, stock, categoría e imagen.

**Tabla CartItems**
Mantiene los items del carrito de compras incluso si se cierra la aplicación.

**Tabla Orders**
Guarda el historial de todos los pedidos realizados, incluyendo datos del cliente y ubicación GPS.

El proyecto incluye un DatabaseSeeder que precarga 10 productos de ferretería al iniciar la app por primera vez.

---

## Validaciones

Todas las validaciones están centralizadas en la clase `FormValidator`, lo que facilita su mantenimiento y evita duplicación de código.

**Validaciones implementadas:**
- Nombres de productos: mínimo 3 caracteres
- Precios: valores numéricos mayores a cero
- Stock: valores enteros no negativos
- URLs: formato válido de URL para imágenes
- Teléfonos: formato chileno (+56 9 XXXX XXXX)

Los formularios muestran mensajes de error específicos y en tiempo real, con iconos descriptivos en cada campo.

---

## Diseño visual

El diseño se inspira en principios de simplicidad y claridad. La paleta de colores fue:

- Primary Blue (#232F3E): Para elementos principales
- Primary Orange (#FF9900): Para acciones destacadas
- Secondary Teal (#37475A): Para elementos secundarios
- Accent Orange (#FF6B35): Para estados activos

La interfaz usa Material Design 3 y todas las pantallas siguen una estructura visual consistente. Los productos se muestran en un grid de dos columnas, optimizado para visualización rápida del catálogo.

---

## Animaciones

Implementé tres tipos de animaciones para mejorar la experiencia de usuario:

1. **Animación de entrada en cascada:** Las tarjetas de la pantalla principal aparecen secuencialmente con un delay progresivo, creando un efecto visual agradable.

2. **Feedback táctil:** Los botones responden con una animación de escala tipo resorte cuando se presionan, simulando el comportamiento de un botón físico. Esta animación fue optimizada usando `mutableFloatStateOf` en lugar de `mutableStateOf` para reducir recomposiciones innecesarias.

3. **Transiciones suaves:** La navegación entre pantallas usa transiciones fluidas que ayudan al usuario a entender el flujo de la aplicación.

El código también aplica optimizaciones de estado usando tipos específicos como `mutableIntStateOf` donde corresponde, mejorando el rendimiento general de la aplicación.

---

## Cómo ejecutar el proyecto

**Requisitos:**
- Android Studio Hedgehog o superior
- JDK 17
- Dispositivo Android con API 24+ o emulador

**Pasos:**

1. Clonar el repositorio
```bash
git clone [https://github.com/feliplvz/construccion-cl-app.git]
cd construccion-cl-app
```

2. Abrir el proyecto en Android Studio y esperar a que sincronice las dependencias

3. Ejecutar en un dispositivo o emulador con el botón Run

**Nota sobre permisos:** La app solicitará permisos de cámara y ubicación en tiempo de ejecución cuando sean necesarios.

---

## Dependencias principales

```gradle
// UI
androidx.compose.material3:material3:1.1.2
androidx.navigation:navigation-compose:2.7.5

// Base de datos
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Async
kotlinx.coroutines.android:1.7.3
androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2

// Imágenes
io.coil-kt:coil-compose:2.5.0

// Recursos nativos
com.google.android.gms:play-services-location:21.0.1
com.google.accompanist:accompanist-permissions:0.32.0
```

