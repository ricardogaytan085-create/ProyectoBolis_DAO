# Plan de Proyecto: Sistema de Control de Asistencia y Ventas de Bolis

Este documento detalla la planificación y estructuración del proyecto que integra el sistema de registro de entrada/salida de usuarios y el catálogo de ventas de bolis. El proyecto se desarrollará en **Java (JavaFX)** utilizando el patrón **MVC** y persistencia en una base de datos **Apache Derby**.

---

## 🎯 Principios de Desarrollo y Buenas Prácticas
*   **Arquitectura MVC:** Respetar la separación estricta entre Modelo, Vista y Controlador.
*   **Simplicidad Extrema:** Mantener el código lo más simple, básico y conciso posible, minimizando las líneas de código. Evitar código innecesario o redundante ("código basura").
*   **Buenas Prácticas:** Código limpio, estructurado y legible.
*   **Transparencia:** No crear archivos ni escribir código sin que sepamos exactamente qué hacen y para qué sirven.

---

## 🏗️ Especificaciones del Sistema

### 1. Modelo de Datos (Base de Datos Derby)
*   **Usuarios:** Gestión de cuentas con roles diferenciados: `Empleado` (usuario común) y `Administrador`.
*   **Asistencia:** Registro automático de las horas de entrada y salida de cada empleado.
*   **Catálogo de Bolis:** Inventario de bolis con ID, sabor, precio y stock disponible.
*   **Ventas:** Registro detallado de cada venta (boli vendido, cantidad, subtotal, usuario que vendió y fecha/hora).

### 2. Interfaz Gráfica (JavaFX)
*   **Pantalla de Login:** Control de acceso según credenciales.
*   **Dashboard del Empleado (Vista Común):**
    *   Reloj digital en tiempo real.
    *   Botón para registrar entrada y salida de turno.
    *   Módulo interactivo para registrar ventas de bolis con actualización de stock en tiempo real.
*   **Dashboard del Administrador:**
    *   CRUD completo de Usuarios (crear, editar, eliminar, listar).
    *   CRUD completo del Catálogo de Bolis (sabores, precios, stock).
    *   Buscador e historial de asistencias de empleados.
    *   Módulo de reportes financieros y exportación a archivos CSV/TXT.

---

## 📅 Etapas de Desarrollo del Proyecto

### Etapa 1: Configuración de Base de Datos (Derby)
1.  **Paso 1.1:** Configurar la conexión JDBC embebida para Apache Derby.
2.  **Paso 1.2:** Crear la tabla `usuarios` (id, login, password, nombre, rol).
3.  **Paso 1.3:** Crear la tabla `asistencia` (id, usuario_id, fecha, hora_entrada, hora_salida).
4.  **Paso 1.4:** Crear la tabla `bolis` (id, sabor, precio, stock).
5.  **Paso 1.5:** Crear la tabla `ventas` (id, boli_id, usuario_id, cantidad, total_venta, fecha_hora).
6.  **Paso 1.6:** Escribir una clase auxiliar de utilidad (`DatabaseInitializer`) para automatizar la creación de tablas si no existen e insertar datos semilla iniciales (usuario administrador por defecto y bolis base).

### Etapa 2: Estructura del Proyecto (MVC) y Dependencias
1.  **Paso 2.1:** Configurar un proyecto Maven con soporte para JavaFX.
2.  **Paso 2.2:** Declarar las dependencias necesarias en `pom.xml` (JavaFX SDK, Derby Driver, etc.).
3.  **Paso 2.3:** Crear la estructura de paquetes lógica:
    *   `com.uaemex.bolis.model` (entidades y lógica de base de datos / DAOs).
    *   `com.uaemex.bolis.view` (vistas FXML e interfaces gráficas).
    *   `com.uaemex.bolis.controller` (lógica de interacción y controladores).

### Etapa 3: Desarrollo del Modelo (Model)
1.  **Paso 3.1:** Crear las clases de dominio/entidad: `Usuario`, `Asistencia`, `Boli`, y `Venta`.
2.  **Paso 3.2:** Implementar la interfaz de Acceso a Datos (DAO) e implementaciones para la base de datos Derby (ej: `UsuarioDAO`, `BoliDAO`, `AsistenciaDAO`, `VentaDAO`).
3.  **Paso 3.3:** Añadir validaciones en el modelo (formato de datos, cantidad de stock disponible suficiente para realizar una venta).

### Etapa 4: Construcción de Interfaces Gráficas (View)
1.  **Paso 4.1:** Diseñar la vista de Login en FXML.
2.  **Paso 4.2:** Diseñar el Dashboard del Empleado (reloj, botones de asistencia, catálogo visual e inputs para registrar ventas).
3.  **Paso 4.3:** Diseñar el Dashboard del Administrador (secciones separadas para gestión de personal, inventario de bolis y visualización de reportes).
4.  **Paso 4.4:** Crear estilos en CSS para asegurar un diseño cohesivo y profesional.

### Etapa 5: Lógica de Negocio e Interacción (Controller)
1.  **Paso 5.1:** Implementar `LoginController` para validar credenciales y redirigir al Dashboard adecuado según el rol del usuario.
2.  **Paso 5.2:** Implementar `EmpleadoController` que controle el reloj, registre la marca de entrada/salida y procese ventas de bolis actualizando la tabla correspondiente.
3.  **Paso 5.3:** Implementar `AdminController` para gestionar el CRUD de usuarios y bolis, y actualizar dinámicamente las tablas en la vista.
4.  **Paso 5.4:** Programar los filtros de fechas e IDs en la sección de reportes.

### Etapa 6: Pruebas, Ajustes y Exportación
1.  **Paso 6.1:** Validar el flujo de navegación entre pantallas sin pérdida de estado.
2.  **Paso 6.2:** Realizar pruebas de transacciones concurrentes (por ejemplo, comprobar que el stock de bolis disminuya adecuadamente tras una venta).
3.  **Paso 6.3:** Implementar la lógica para exportar reportes de ventas y asistencias a archivos planos (`CSV`/`TXT`).
4.  **Paso 6.4:** Compilar y empaquetar la aplicación en un archivo `.jar` ejecutable.
