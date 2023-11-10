1. JwtAuthenticationConverter:
   Esta clase implementa la interfaz Converter y se utiliza para convertir un token JWT en un token de autenticación de Spring.

Extrae las autoridades y otros detalles del token JWT y los utiliza para construir un JwtAuthenticationToken.

2. SecurityConfig:
   Configura la seguridad de la aplicación utilizando Spring Security.

Deshabilita CSRF, autoriza todas las solicitudes autenticadas y configura el servidor de recursos OAuth2.

Utiliza el bean JwtAuthenticationConverter para convertir tokens JWT en tokens de autenticación de Spring.

3. Controladores y Servicios:
   KeycloakController: Expone endpoints REST para buscar, crear, actualizar y eliminar usuarios en Keycloak.

KeycloakServiceImpl: Implementa la lógica para interactuar con Keycloak, como buscar usuarios, crear usuarios, actualizar usuarios y eliminar usuarios.

4. KeyCloakProvider:
   Proporciona métodos estáticos para obtener instancias de RealmResource y UsersResource de Keycloak.
5. Configuración de Keycloak:
   Se configura el servidor de recursos JWT para validar tokens con Keycloak.

Se establece el URI del emisor y el URI del conjunto de claves JSON Web Key (JWK).

6. Configuración de log:
   Se configura el nivel de log para la categoría org.keycloak en DEBUG.
7. Clase UserDTO:
   Representa los datos de usuario utilizados en las operaciones de creación y actualización de usuarios.
8. Anotaciones de Seguridad:
   @PreAuthorize("hasRole('admin_client_role')"): Anotación que asegura que solo los usuarios con el rol específico (admin_client_role) puedan acceder a los endpoints anotados.


Clases y Relaciones:
Usuario:

Representa a un usuario autenticado en el sistema.
Contiene información sobre el cliente.
Direccion:

Representa la dirección asociada a una compra o entrega.
MetodoPago:

Representa el método de pago asociado a una compra.
Compra:

Contiene información sobre los productos que el cliente quiere comprar.
Tiene asociado un cliente, dirección y método de pago.
Puede tener múltiples productos.
Tiene un estado que indica si está en proceso o completada.
Pedido:

Se crea a partir de una compra.
Contiene información de la compra asociada.
Tiene un estado que indica si está en proceso o completado.
Entrega:

Se crea a partir de un pedido.
Contiene información de la dirección de entrega y el estado de la entrega.
Endpoints Iniciales:
Compra:

POST /api/compras/iniciar (Iniciar una nueva compra con identificación de cliente y producto)
POST /api/compras/{id}/agregar-producto (Agregar producto a la compra)
PUT /api/compras/{id}/modificar-producto (Modificar la cantidad de un producto en la compra)
DELETE /api/compras/{id}/eliminar-producto (Eliminar producto de la compra)
PUT /api/compras/{id}/cambiar-direccion (Cambiar la dirección asociada a la compra)
PUT /api/compras/{id}/cambiar-metodo-pago (Cambiar el método de pago asociado a la compra)
GET /api/compras/{id} (Obtener información detallada de la compra)
Pedido:

POST /api/pedidos/crear (Crear un nuevo pedido a partir de una compra)
GET /api/pedidos/{id} (Obtener información detallada del pedido)
Entrega:

POST /api/entregas/crear (Crear una nueva entrega a partir de un pedido)
GET /api/entregas/{id} (Obtener información detallada de la entrega)
Autenticación:

POST /api/auth/login (Iniciar sesión de usuario)
Observaciones:
Se debe implementar la autenticación para que los usuarios tengan acceso a los endpoints de Compra, Pedido y Entrega.
Utilizar DTOs para manejar las operaciones y respuestas de los endpoints.
Se puede utilizar Spring Security para la autenticación y autorización de usuarios.
Implementar servicios y repositorios para manejar la lógica de negocio y acceder a la base de datos.
Asegurarse de lograr al menos un 50% de cobertura de código mediante pruebas unitarias y de integración.
Utilizar herramientas como Flyway o Liquibase para inicializar la base de datos con datos de prueba.