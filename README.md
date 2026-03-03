# 🚀 Enterprise Management System API

¡Hola! Este es un ecosistema **Backend robusto** desarrollado con **Spring Boot 3.5**, diseñado para la gestión integral de capital humano, contratos y flujos financieros empresariales. 

Este proyecto no es solo un CRUD; es una implementación de estándares de **seguridad industrial**, almacenamiento en la nube y arquitectura limpia, pensada para escalar en entornos de producción reales.

---

### 🛠️ Stack Tecnológico & Superpoderes
* **Core:** Java 17 & Spring Boot 3.5.x
* **Seguridad:** **Spring Security & JWT** (Autenticación Stateless + RBAC).
* **Persistencia:** Spring Data JPA con soporte para PostgreSQL / MySQL / H2.
* **Cloud:** **Cloudinary SDK** para gestión profesional de archivos multimedia.
* **Documentación:** **Swagger / OpenAPI 3** (Documentación interactiva y viva).
* **Calidad de Código:** Lombok (Clean code) & Global Exception Handling.

---

### 🏗️ Arquitectura y Patrones (Clean Code)
He aplicado principios de ingeniería de software para asegurar un mantenimiento a largo plazo:
* **S.O.L.I.D:** Especialmente *Single Responsibility* al desacoplar seguridad, servicios de terceros y lógica de negocio.
* **DTO Pattern:** Protección de la capa de persistencia mediante objetos de transferencia.
* **Security by Design:** Implementación de **CORS dinámico** mediante variables de entorno para despliegues seguros.

---

### 🔐 Gestión de Accesos (RBAC)
El sistema implementa una jerarquía de acceso estricta para proteger datos sensibles:

| Rol | Permisos |
| :--- | :--- |
| 👑 **ADMIN** | Acceso total: Estadísticas financieras, CRUD de empleados, gestión de contratos y roles. |
| 💼 **MANAGER** | Edición de empleados y visualización de reportes operativos. |
| 👤 **USER** | Acceso a perfil propio y lectura de datos básicos autorizados. |

* **Seguridad Extra:** Encriptación de claves con **BCrypt** y validación de identidad en cambios de contraseña.

---

### 📊 Descripción de Endpoints (API Methods)

| Método | Endpoint | Descripción | Requisito de Rol |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/**` | Registro y Login. Genera el Token JWT. | 🌍 Público |
| `GET` | `/api/v1/employees` | Lista todos los empleados con filtrado dinámico. | 🔐 Authenticated |
| `POST` | `/api/v1/employees` | Crea un nuevo empleado e integra foto vía Cloudinary. | 👑 ADMIN |
| `PATCH` | `/api/v1/employees/{id}/password` | Permite al usuario o Admin cambiar la contraseña. | 👑 ADMIN / Owner |
| `PUT` | `/api/v1/employees/{id}` | Actualización completa de datos del empleado. | 💼 ADMIN / MANAGER |
| `GET` | `/api/v1/stats/**` | Métricas de salarios, flujo de caja y contratos activos. | 👑 ADMIN |
| `GET` | `/swagger-ui.html` | Interfaz interactiva para pruebas de la API. | 🌍 Público |

---

### 📈 Funcionalidades de Alto Impacto
* **Cloudinary Integration:** Procesamiento asíncrono de imágenes. Las fotos de perfil se procesan en la nube, optimizando el almacenamiento local.
* **Stateless Security:** Escalabilidad horizontal gracias al uso de JWT.
* **Global Exception Handling:** Centralización de errores para respuestas consistentes en formato JSON.

---

### 🚀 Instalación y Despliegue
1. **Clonar:** `git clone https://github.com/MariaL2000/envy-company.git`
2. **Configurar:** Define tus variables de entorno en `application.properties`:
   ```properties
   JWT_SECRET=${TU_SECRETO}
   CLOUDINARY_CLOUD_NAME=${TU_CLOUD}
