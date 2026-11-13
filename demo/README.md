# Sistema de Pedidos de Imprenta (JavaFX + MySQL)

Proyecto JavaFX con arquitectura por capas para gestionar los pedidos de una imprenta.

- **Estudiante:** Elvis Leonardo Venturo Prieto
- **Tabla:** `Impreta` (base de datos `imprenta_db`)
- **Campos:** `id_pedido`, `nombre_cliente`, `tipo_trabajo`, `cantidad`, `formato`, `fecha_pedido`, `fecha_entrega`, `estado`

## Estructura de paquetes

```
src/main/java/vallegrande/edu/pe/demo/
├── HelloApplication.java        # Punto de entrada JavaFX
├── Launcher.java
├── model/
│   ├── Pedido.java              # Entidad (modelo)
│   ├── PedidoDAO.java           # CRUD contra MySQL
│   └── Conexion.java            # Conexión JDBC
├── controller/
│   └── PedidoController.java    # Lógica de la vista (eventos)
└── (view en resources)
src/main/resources/vallegrande/edu/pe/demo/view/
├── pedido-view.fxml             # Interfaz gráfica
└── estilos.css                  # Estilos

ejemplotablas.sql                # Script de base de datos y tablas
```

## 1. Crear el contenedor MySQL con Docker

```bash
docker run --name mysql-imprenta ^
  -e MYSQL_ROOT_PASSWORD=123456 ^
  -e MYSQL_DATABASE=imprenta_db ^
  -p 3306:3306 ^
  -v mysql_imprenta_data:/var/lib/mysql ^
  -d mysql:8.4
```

Comprobar que está corriendo:

```bash
docker ps
```

Ver los logs (esperar el mensaje "ready for connections"):

```bash
docker logs mysql-imprenta
```

Comandos útiles:

```bash
docker stop mysql-imprenta     # detener
docker start mysql-imprenta    # iniciar de nuevo
docker rm -f mysql-imprenta    # eliminar contenedor
```

## 2. Crear las tablas

Opción A (recomendada): ejecutar el script desde MySQL Workbench.

1. Abrir MySQL Workbench → **Database** → **Connect to Database** con:
   - Hostname: `127.0.0.1`
   - Port: `3306`
   - Username: `root`
   - Password: `123456`
2. Abrir el archivo `ejemplotablas.sql` y ejecutarlo.

Opción B (desde consola):

```bash
docker exec -i mysql-imprenta mysql -uroot -p123456 < ejemplotablas.sql
```

## 3. Conectar MySQL Workbench al contenedor

- Connection Method: **Standard (TCP/IP)**
- Hostname: `127.0.0.1` (o `localhost`)
- Port: `3306`
- Username: `root`
- Password: `123456`
- Default Schema: `imprenta_db`

En `Conexion.java` ya están configurados esos mismos datos
(`localhost:3306`, usuario `root`, clave `123456`, base `imprenta_db`).

## 4. Ejecutar la aplicación

```bash
mvnw.cmd clean javafx:run
```

En Linux/macOS:

```bash
./mvnw clean javafx:run
```

## Funcionalidades

1. Registrar pedidos.
2. Consultar los pedidos en una tabla.
3. Actualizar el pedido seleccionado.
4. Eliminar con confirmación.
5. Buscar por cliente, tipo de trabajo o estado.
6. Validaciones de datos y mensajes de error.
