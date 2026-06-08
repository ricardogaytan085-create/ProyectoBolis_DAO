# Propuesta de proyecto con vistas gráficas (GUI)

## Sistema de registro de entrada y salida en Java

### 1. Descripción general
Desarrollar una aplicación de escritorio con interfaz gráfica (vistas) que permita registrar la hora de entrada y salida de usuarios, consultar historiales y generar reportes. El sistema utilizará el patrón MVC (Modelo-Vista-Controlador) para separar la lógica de negocio, la persistencia y la interfaz de usuario.

### 2. Objetivos
* Implementar un sistema funcional con registro intuitivas.
* Aplicar programación orientada a eventos y manejo de componentes Swing.
* Garantizar persistencia de datos (archivos plano).
* Ofrecer vistas diferenciadas según el rol (usuario común / administrador).

### 3. Vistas principales (propuesta)

| Vista | Descripción | Componentes clave |
| :--- | :--- | :--- |
| **Login** | Autenticación de usuarios (opcional) | Campos usuario/contraseña, botón “Ingresar” |
| **Vista principal (Dashboard)** | Panel con opciones rápidas | Botones: Registrar Entrada, Registrar Salida, Consultar Registros, Gestionar Usuarios (solo admin), Salir. También muestra reloj en tiempo real. |
| **Registro de entrada/salida** | Formulario para marcar evento | Campo para ID del empleado/visitante, selector de fecha/hora automático, botón “Registrar Entrada” y “Registrar Salida”. Muestra el último registro del usuario. |
| **Consulta de registros** | Tabla con filtros de fechas | Tabla (JTable) con columnas: ID, Nombre, Fecha, Hora entrada, Hora salida, Horas permanencia. Filtros: rango de fechas, buscar por ID. Botón “Generar reporte”. |
| **Gestión de usuarios (solo admin)** | CRUD de personas | Campos: ID, Nombre, Rol (usuario/admin). Botones: Agregar, Modificar, Eliminar, Listar. |
| **Reportes** | Vista emergente o diálogo | Muestra resumen: total de horas por usuario en un período, gráfico de barras simple (opcional). Botón “Exportar a TXT/CSV/PDF”. |

### 4. Flujo de navegación sugerido

text

Login → Dashboard → [Entrada / Salida / Consultas / Gestión]

Desde cada vista secundaria se puede regresar al Dashboard. El cierre de sesión vuelve al Login.

### 5. Tecnologías recomendadas
* **Java** (versión 8 o superior)
* **Interfaz gráfica**: Swing (con `JFrame`, `JPanel`, `JTable`, `CardLayout`) o JavaFX (con `Scene Builder` para diseño visual)
* **Persistencia**:
* **Opción**: Archivos CSV/TXT (simple, ideal para proyectos académicos)
* **Manejo de fechas/horas**: `java.time.LocalDateTime` y `DateTimeFormatter`
* **Patrón de diseño**: MVC (Modelo: clases `Registro`, `Persona`; Vista: clases de interfaz; Controlador: eventos y lógica de negocio)

### 6. Entregables esperados
* Código fuente completo (estructura de paquetes clara).
* Manual de usuario breve (PDF o texto).
* Diagrama de clases y de secuencia.
* Ejecutable `.jar` o instrucciones de compilación.

### 7. Criterios de evaluación adicionales (sobre las vistas)

| Aspecto | Ponderación |
| :--- | :--- |
| Diseño y usabilidad de las ventanas (layouts, tamaños, coherencia) | 25% |
| Validación de datos desde la interfaz (mensajes de error en diálogos) | 15% |
| Actualización dinámica de componentes (ej. recarga de tabla tras nuevo registro) | 20% |
| Navegación fluida entre vistas (sin pérdida de estado) | 15% |
| Manejo de eventos y separación MVC | 25% |

### 8. Posibles extensiones (bonificación)
* Uso de JavaFX con FXML y `Scene Builder`.
* Generación de reportes en PDF con iText o Apache PDFBox.
* Gráficos simples (JFreeChart) para horas acumuladas.
* Notificaciones emergentes al registrar entrada/salida.

### 9. Ejemplo de captura (descripción visual)

```text
+--------------------------------------------------+
| Sistema de Registro – Entrada y Salida de Usuarios |
+--------------------------------------------------+
| [ Reloj: 15:30:45 - 12/05/2026 ]                 |
|                                                  |
| [ Registrar Entrada ] [ Registrar Salida ]       |
| [ Consultar ]         [ Gestionar Personas ]     |
| [ Reportes ]          [ Salir ]                  |
|                                                  |
| Último registro: Juan Pérez entró a las 15:27    |
+--------------------------------------------------+
```
