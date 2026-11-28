# 🍽️ RestoSync
 
**RestoSync** es una aplicación Android para la gestión de comandas en hostelería. Está pensada para que el camarero pueda abrir mesas, tomar nota de los pedidos y consultar la cuenta de cada mesa desde el móvil, de forma rápida y visual.
 
> Proyecto del módulo **Programación Multimedia y Dispositivos Móviles (PGL)** · 2.º DAM · CIFP Villa de Agüimes.
> Autora: **Jacqueline E. Díaz Hadjar**
 
---
 
## 📋 Índice
 
- [Funcionalidades](#-funcionalidades)
- [Capturas de pantalla](#-capturas-de-pantalla)
- [Tecnologías](#️-tecnologías)
- [Arquitectura](#️-arquitectura)
- [Base de datos](#️-base-de-datos)
- [Navegación](#-navegación)
- [Instalación y ejecución](#️-instalación-y-ejecución)
- [Limitaciones y mejoras futuras](#-limitaciones-y-mejoras-futuras)
- [Uso de Inteligencia Artificial](#-uso-de-inteligencia-artificial)
---
 
## ✨ Funcionalidades
 
- **Inicio de sesión** con formulario de usuario y contraseña *(simulado, ver [limitaciones](#-limitaciones-y-mejoras-futuras))*.
- **Mapa de mesas** en cuadrícula, cada una con el icono de su forma (cuadrada, redonda, rectangular…). Las mesas abiertas se distinguen por color y muestran el número de comensales.
- **Abrir mesa**: al pulsar una mesa libre se piden los comensales y se crea automáticamente su comanda.
- **Toma de pedidos** con un menú lateral de categorías (Entrantes, Ensaladas, Pizzas, Pastas, Postres y Bebidas) y una cuadrícula de platos con foto, nombre y precio. Cada pulsación añade el plato a la comanda de la mesa.
- **Resumen de comandas**: lista de todas las comandas con su mesa y su estado (activa o cerrada), con opción de eliminarlas.
- **Detalle de la comanda**: platos agrupados por cantidad (`2x Tiramisú`), precio por unidad, subtotal y **total de la cuenta** actualizado en tiempo real. Permite quitar unidades y añadir más platos desde el botón **+**.
- **Cerrar sesión** desde la pantalla de mesas.
---
 
## 📸 Capturas de pantalla
 
| Inicio de sesión | Mesas | Elección de platos |
|:---:|:---:|:---:|
| <img src="capturas/login.png" width="230"/> | <img src="capturas/mesas.png" width="230"/> | <img src="capturas/pedido.png" width="230"/> |
 
| Abrir mesa | Resumen de comandas | Detalle de la comanda |
|:---:|:---:|:---:|
| <img src="capturas/comensales.png" width="230"/> | <img src="capturas/comandas.png" width="230"/> | <img src="capturas/detalle.png" width="230"/> |
 
---
 
## 🛠️ Tecnologías
 
| Tecnología | Uso |
|---|---|
| **Kotlin** 2.2 | Lenguaje principal |
| **Jetpack Compose** + Material 3 | Interfaz de usuario declarativa |
| **Room** 2.8 | Base de datos local SQLite |
| **Navigation Compose** 2.8 | Navegación entre pantallas con argumentos |
| **ViewModel** + **StateFlow** | Gestión del estado según el patrón MVVM |
| **Kotlin Coroutines** + **Flow** | Operaciones asíncronas y datos reactivos |
| **KSP** | Generación del código de Room |
 
- **SDK mínimo:** 24 (Android 7.0)
- **SDK objetivo:** 37
---
 
## 🏗️ Arquitectura
 
El proyecto sigue el patrón **MVVM (Model–View–ViewModel)**:
 
```
app/src/main/java/com/example/pgl_restosync/
├── MainActivity.kt                  → Punto de entrada: crea el ViewModel y lanza la navegación
├── RestoSyncApp.kt                  → Clase Application: crea la base de datos y el repositorio una sola vez
│
├── Model/
│   ├── RestauranteRepository.kt     → Intermediario entre los DAOs y los ViewModels
│   └── Local/
│       ├── AppDatabase.kt           → Base de datos Room (Singleton)
│       ├── MesaEntity.kt            → Tabla de mesas
│       ├── FormaMesa.kt             → Enum con las formas de mesa
│       ├── CategoriaEntity.kt       → Tabla de categorías de la carta
│       ├── PlatoEntity.kt           → Tabla de platos y bebidas
│       ├── ComandaEntity.kt         → Cabecera de cada comanda
│       ├── ComandaDetalleEntity.kt  → Líneas (platos) de cada comanda
│       └── *Dao.kt                  → Consultas de cada tabla
│
├── ViewModel/
│   ├── ViewModelRestaurante.kt          → ViewModel compartido: mesas, carta y comandas
│   ├── RestauranteViewModelFactory.kt
│   ├── DetalleComandaViewModel.kt       → ViewModel propio de la pantalla de detalle
│   ├── DetalleComandaViewModelFactory.kt
│   └── DetalleUiState.kt                → Estados de la pantalla de detalle (Cargando / Éxito / Error)
│
└── View/
    ├── Navegacion.kt        → Rutas (sealed class), NavHost y barra inferior
    ├── Inicio.kt            → Pantalla de inicio de sesión
    ├── Home.kt              → Mapa de mesas y diálogo de comensales
    ├── Pedido.kt            → Elección de platos con menú lateral de categorías
    ├── ResumenPedidos.kt    → Lista de comandas
    └── DetalleComanda.kt    → Detalle y total de una comanda
```
 
**Flujo de datos:** las pantallas observan `StateFlow` expuestos por los ViewModels. Los ViewModels piden los datos al repositorio, que a su vez consulta Room mediante los DAOs. Como las consultas de lectura devuelven `Flow`, cualquier cambio en la base de datos se refleja automáticamente en pantalla, sin recargar.
 
**ViewModels:**
- `ViewModelRestaurante` se crea en `MainActivity` y se comparte entre las pantallas principales.
- `DetalleComandaViewModel` se crea dentro de la ruta del detalle, por lo que solo vive mientras esa pantalla está abierta. Gestiona su estado con un `sealed interface` (`DetalleUiState`).
- Ambos se crean mediante su **Factory**, que recibe el repositorio guardado en `RestoSyncApp`.
### 🔄 Detalle técnico: cambio de categoría con `flatMapLatest`
 
En la pantalla de elección de platos, al pulsar una categoría del menú lateral, la cuadrícula muestra solo los platos de esa categoría, sin recargar la pantalla ni lanzar la consulta a mano. Esto se consigue con el operador `flatMapLatest` de Kotlin Flow.
 
**¿Qué hace?** Escucha un `Flow` (la categoría seleccionada) y, cada vez que emite un valor nuevo, lo transforma en **otro `Flow`** (la consulta de platos de esa categoría). Además, **cancela la consulta anterior** y se queda solo con la última (*latest*), así nunca llegan platos de una categoría que ya no está seleccionada.
 
**1. La consulta en el DAO** (`PlatoDao.kt`) devuelve un `Flow` filtrado por categoría:
 
```kotlin
@Query("SELECT * FROM platos WHERE categoriaId = :categoriaId")
fun obtenerPlatosPorCategoria(categoriaId: Int): Flow<List<PlatoEntity>>
```
 
**2. El ViewModel** (`ViewModelRestaurante.kt`) guarda la categoría seleccionada y encadena la consulta con `flatMapLatest`:
 
```kotlin
// Categoría pulsada en el menú lateral (empieza en Entrantes = 1)
private val _categoriaSeleccionada = MutableStateFlow(1)
val categoriaSeleccionada: StateFlow<Int> = _categoriaSeleccionada
 
// Platos de la categoría seleccionada: cambian solos al cambiar de categoría
@OptIn(ExperimentalCoroutinesApi::class)
val platos: StateFlow<List<PlatoEntity>> = _categoriaSeleccionada
    .flatMapLatest { categoriaId -> repository.obtenerPlatosPorCategoria(categoriaId) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
 
fun seleccionarCategoria(categoriaId: Int) {
    _categoriaSeleccionada.value = categoriaId
}
```
 
**3. La vista** (`Pedido.kt`) solo avisa al ViewModel de qué categoría se ha pulsado y pinta la lista de platos que recibe:
 
```kotlin
val platos by viewModel.platos.collectAsState()
 
NavigationRailItem(
    selected = categoria.id == categoriaSeleccionada,
    onClick = { viewModel.seleccionarCategoria(categoria.id) },
    // ...
)
```
 
**Flujo completo al pulsar "Pizzas":**
 
1. La vista llama a `seleccionarCategoria(3)`.
2. `_categoriaSeleccionada` pasa a valer `3`.
3. `flatMapLatest` cancela la consulta de la categoría anterior y lanza `obtenerPlatosPorCategoria(3)`.
4. Room devuelve las pizzas, `stateIn` las convierte en `StateFlow` y la cuadrícula se repinta sola.
**¿Por qué no un `map` normal?** Porque `map` transformaría cada categoría en un `Flow` y el resultado sería un `Flow` dentro de otro (`Flow<Flow<List<PlatoEntity>>>`), que la vista no puede pintar. `flatMapLatest` "aplana" ese resultado en un único `Flow<List<PlatoEntity>>`. La anotación `@OptIn(ExperimentalCoroutinesApi::class)` es necesaria porque el operador está marcado como experimental en la librería de corrutinas, aunque su uso es muy habitual.
 
### 🧩 Otros detalles técnicos
 
#### Imágenes enlazadas a los datos: `enum` + funciones de traducción
 
Cada mesa, categoría y plato tiene una imagen. En lugar de escribir cada tarjeta a mano en la vista, la imagen se obtiene **a partir de los datos** de la base de datos mediante funciones que "traducen" un dato a un recurso de `R.drawable`.
 
**Mesas: con un `enum`.** La forma de la mesa se guarda como un dato más de la tabla `mesas`, usando el enum `FormaMesa` (`FormaMesa.kt`):
 
```kotlin
enum class FormaMesa {
    CUADRADA,
    REDONDA_CUATRO_ASIENTOS,
    REDONDA_CINCO_ASIENTOS,
    RECTANGULAR
}
```
 
```kotlin
@Entity(tableName = "mesas")
data class MesaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val numero: String,
    val forma: FormaMesa = FormaMesa.CUADRADA,
    val comensales: Int = 0,
    val isOpen: Boolean = false
)
```
 
Al insertar cada mesa se indica su forma, y la vista (`Home.kt`) la traduce a su icono:
 
```kotlin
mesaDao.insertarMesas(MesaEntity(id = 2, numero = "Mesa 2", forma = FormaMesa.REDONDA_CUATRO_ASIENTOS))
```
 
```kotlin
private fun iconoMesa(forma: FormaMesa): Int = when (forma) {
    FormaMesa.CUADRADA -> R.drawable.mesa_cuadrada_icono
    FormaMesa.REDONDA_CUATRO_ASIENTOS -> R.drawable.mesa_redonda_cuatro_asientos
    FormaMesa.REDONDA_CINCO_ASIENTOS -> R.drawable.mesa_redonda_icono
    FormaMesa.RECTANGULAR -> R.drawable.mesa_rectangular
}
```
 
**Categorías y platos: por su id.** Como la carta es fija, cada categoría y cada plato se insertan con un **id fijo** (del 1 al 6 y del 1 al 12), y la vista (`Pedido.kt`) asocia cada id con su icono o su foto:
 
```kotlin
platoDao.insertarPlatos(PlatoEntity(id = 9, nombre = "Tiramisú", precio = 7.99, categoriaId = 5))
```
 
```kotlin
private fun imagenPlato(platoId: Int): Int = when (platoId) {
    1 -> R.drawable.hummus_de_berenjena
    // ...
    9 -> R.drawable.tiramisu
    // ...
    else -> R.drawable.logo_del_proyecto__app_   // por si algún plato no tiene foto
}
```
 
**¿Por qué así?**
 
- **Separación de capas (MVVM):** el *Model* solo guarda datos ("esta mesa es redonda", "este plato es el 9") y la *View* decide cómo se dibujan. Si se cambian los dibujos, solo se toca la vista.
- **No se guarda `R.drawable` en la base de datos:** los números de `R` los genera Android Studio **al compilar** y pueden cambiar de una compilación a otra. Si se guardaran en Room, otro día podrían apuntar a otra imagen. La forma o el id, en cambio, son datos propios que no cambian.
- **El `enum` evita errores silenciosos:** con un `String` (por ejemplo, `"redonda"`), una errata como `"redona"` no daría error y mostraría la imagen por defecto sin avisar. Con el enum, una errata no compila. Además, el `when` cubre todos los valores posibles y **no necesita `else`**: si en el futuro se añade una forma nueva al enum, el compilador obliga a asignarle un icono.
- **Ids fijos en los inserts:** como la base de datos se reinicia en cada arranque, con `autoGenerate` los ids irían cambiando (13, 14, 15…) y ninguna imagen cuadraría. Con ids fijos, el plato 9 es siempre el tiramisú.
- **Mesas automáticas:** gracias a todo esto, la cuadrícula de mesas se genera sola con un `LazyVerticalGrid` y `items(listaMesas)`. Añadir una mesa nueva es escribir una sola línea en el repositorio.
#### Título seguro con `as?`, `?.` y `?:`
 
En la pantalla de detalle, el nombre de la mesa solo se conoce cuando los datos han terminado de cargar (`DetalleComanda.kt`):
 
```kotlin
val titulo = (uiState as? DetalleUiState.Exito)?.numeroMesa ?: "Comanda"
```
 
Se leen tres operadores de Kotlin seguidos:
 
1. **`as?` (conversión segura):** intenta tratar el estado como `Exito`. Si el estado es `Cargando` o `Error`, en vez de cerrar la app con una excepción, devuelve `null`.
2. **`?.` (llamada segura):** solo lee `numeroMesa` si lo anterior no es `null`; si lo es, todo da `null`.
3. **`?:` (operador Elvis):** si el resultado es `null`, usa el valor por defecto `"Comanda"`.
Resultado: mientras carga, la barra superior muestra **"Comanda"**, y en cuanto llegan los datos cambia sola a **"Mesa 3"**, sin riesgo de error.
 
#### Textos largos con `TextOverflow.Ellipsis`
 
Algunos platos tienen nombres muy largos (*"Gnocchi molto molto gorgonzola con jamón ibérico"*), que rompían el diseño de las tarjetas. En `Pedido.kt` y `DetalleComanda.kt`:
 
```kotlin
Text(
    text = plato.nombre,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis
)
```
 
- **`maxLines = 2`** limita el texto a dos líneas como máximo.
- **`overflow = TextOverflow.Ellipsis`** indica qué hacer con lo que no cabe: en vez de cortarlo de golpe, añade **"…"** al final (*"Gnocchi molto molto gorgonzo…"*), para que se note que el nombre sigue.
#### Fotos cuadradas con `aspectRatio(1f)`
 
Al principio, las fotos de los platos salían recortadas: la tarjeta tenía una altura fija y la foto solo recibía el espacio que le dejaban los textos. Se solucionó haciendo que la **foto marque su propio tamaño** (`Pedido.kt`):
 
```kotlin
Image(
    painter = painterResource(id = imagenPlato(plato.id)),
    contentDescription = plato.nombre,
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
)
```
 
- **`fillMaxWidth()`** hace que la foto ocupe todo el ancho de la tarjeta.
- **`aspectRatio(1f)`** fija la proporción **ancho : alto = 1 : 1**, es decir, la foto es tan alta como ancha, sea cual sea el tamaño de la pantalla. Con `1.3f` sería más ancha que alta.
- **`ContentScale.Crop`** rellena ese cuadrado sin deformar la imagen, recortando solo lo que sobre. Como las fotos originales ya son casi cuadradas, se ven prácticamente enteras.
- La tarjeta **no tiene altura fija**: mide lo que necesiten la foto y los textos.
---
 
## 🗄️ Base de datos
 
Base de datos local con **Room** (`restosync_room.db`), formada por cinco tablas relacionadas:
 
```mermaid
erDiagram
    MESAS ||--o{ COMANDAS : "tiene"
    COMANDAS ||--o{ COMANDA_DETALLE : "contiene"
    PLATOS ||--o{ COMANDA_DETALLE : "se pide en"
    CATEGORIAS ||--o{ PLATOS : "agrupa"
 
    MESAS {
        int id PK
        string numero
        string forma
        int comensales
        boolean isOpen
    }
    COMANDAS {
        int id PK
        int mesaId FK
        boolean isActiva
    }
    COMANDA_DETALLE {
        int id PK
        int comandaId FK
        int platoId
        int cantidad
    }
    PLATOS {
        int id PK
        string nombre
        double precio
        int categoriaId FK
    }
    CATEGORIAS {
        int id PK
        string nombre
    }
```
 
**Claves foráneas con `CASCADE`:** al eliminar una comanda se eliminan automáticamente todas sus líneas de detalle.
 
**Consultas destacadas:**
- **Resumen de comandas**: `INNER JOIN` entre `comandas` y `mesas` para mostrar el nombre de la mesa de cada comanda, ordenadas con las activas primero.
- **Detalle de la comanda**: `JOIN` entre `comanda_detalle`, `comandas` y `platos`, agrupado con `GROUP BY` y `SUM(cantidad)`. Así, si un plato se pide varias veces, aparece en una sola línea (`3x Estrella Galicia`), y al eliminar se quita una unidad.
- **Platos por categoría**: filtrado por `categoriaId`, combinado en el ViewModel con `flatMapLatest` para que la lista cambie sola al seleccionar otra categoría.
La forma de las mesas se guarda como un **enum** (`FormaMesa`), que Room almacena como texto.
 
**Datos de ejemplo:** al arrancar, la app carga 6 mesas, 6 categorías y 12 platos con sus fotos.
 
---
 
## 🧭 Navegación
 
Las rutas están definidas en un `sealed class Navegacion`:
 
| Ruta | Pantalla | Barra inferior |
|---|---|:---:|
| `login` | Inicio de sesión | ❌ |
| `home` | Mapa de mesas | ✅ |
| `comandas` | Resumen de comandas | ✅ |
| `pedido/{mesaId}` | Elección de platos | ❌ |
| `detalle/{mesaId}` | Detalle de la comanda | ❌ |
 
- Las rutas con `{mesaId}` reciben el id de la mesa como argumento y se construyen con `crearRuta(mesaId)`.
- La barra inferior solo aparece en las pantallas principales, para dejar todo el espacio a la toma de pedidos.
- Al iniciar sesión, la pantalla de login se elimina de la pila (`popUpTo`), y al cerrar sesión se vacía la pila completa.
**Flujo habitual:**
`Login → Mesas → (abrir mesa) → Elección de platos → Comandas → Detalle → (+) → Elección de platos`
 
---
 
## ▶️ Instalación y ejecución
 
1. Clonar el repositorio:
```bash
git clone https://github.com/Terijacquie/PGL_RestoSync.git
```
2. Abrir el proyecto con **Android Studio**.
3. Esperar a que Gradle sincronice las dependencias.
4. Ejecutar en un emulador o dispositivo con **Android 7.0 (API 24)** o superior.
5. En la pantalla de inicio, pulsar **Iniciar sesión** (no hace falta rellenar los campos).
---
 
## 🚧 Limitaciones y mejoras futuras
 
- **Inicio de sesión simulado**: la pantalla de login está maquetada y la navegación funciona, pero todavía no valida usuario y contraseña. Como mejora, se añadiría una tabla de usuarios en Room.
- **Reinicio de datos**: para facilitar las pruebas, la base de datos se vacía y se vuelve a cargar con los datos de ejemplo cada vez que se abre la app.
- **Imágenes de los platos**: se asignan por el id del plato en la vista. Para una carta dinámica, la imagen se guardaría como dato del plato (nombre del recurso o ruta de una foto) y se cargaría con una librería como **Coil**.
- **Cierre de mesa y cobro**: la lógica para cerrar una mesa ya existe en el ViewModel y el repositorio; falta añadir el botón en la pantalla de detalle.
- **Gestión de la carta**: añadir, editar o eliminar platos y categorías desde la propia app.
---
 
## 🤖 Uso de Inteligencia Artificial
 
Durante el desarrollo de este proyecto se ha utilizado **Claude** (Anthropic) como herramienta de apoyo, en concreto para:
 
- **Consultas técnicas complejas**, como el diseño de consultas SQL con `JOIN` y `GROUP BY` en Room, la navegación con argumentos en Compose o el uso de `flatMapLatest` para que los platos cambien al seleccionar una categoría (ver [detalle técnico](#-detalle-técnico-cambio-de-categoría-con-flatmaplatest)).
- **Detección y corrección de errores** de compilación y de ejecución.
- **Optimización y refactorización de código**, para hacerlo más limpio y reutilizable.
- **Estructuración y redacción de este README**, organizando la documentación del proyecto en apartados.
La idea de la aplicación, su diseño y la estructura del proyecto son propios. El código sugerido por la IA se ha revisado, comprendido y adaptado al proyecto.
 
---
 
<p align="center">Hecho con ☕ y Kotlin por <b>Jacqueline E. Díaz Hadjar</b></p>
