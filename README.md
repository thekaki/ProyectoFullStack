🛠️ Nuevo proyecto: Back-end de gestión de artículos con control de acceso personalizado

Hoy quiero compartir una mirada general a un proyecto en el que he estado trabajando recientemente. No es perfecto ni terminado, pero ha sido una gran oportunidad para aprender y aplicar buenas prácticas reales.

📌 Qué hace el proyecto

Es una API / servicio backend para gestionar “artículos”: creación, consulta, borrado, filtrado por título o propietario.

Incluye control de acceso fino: cada operación crítica (por ejemplo borrar un artículo) está protegida con lógica propia que comprueba si el usuario es el propietario del recurso o tiene un permiso asignado.

La seguridad se basa en Spring Security / JWT: los usuarios se autentican con tokens, y luego cada llamada verifica permisos dinámicamente.

Implementé una capa de autorización personalizada: un bean que decide si un usuario puede hacer una acción basándose en su rol, permisos y si es propietario del recurso (modelo «Ownable»).

💡 Tecnologías utilizadas

Algunas de las herramientas clave que he usado:

Java + Spring Boot como núcleo backend (controladores, servicios, repositorios)

Spring Security + JWT para manejo de autenticación/autorización

Persistencia con JPA / Hibernate para entidades en base de datos

Arquitectura orientada a capas (controladores, servicios, repositorios)

Buenas prácticas como separar lógica de negocios, comprobaciones de seguridad, reutilización de componentes

Logs y trazas para depurar decisiones de seguridad

🧠 Lo que aprendí / retos interesantes

Cómo integrar reglas de negocio con seguridad personalizada — por ejemplo que un usuario pueda eliminar solo sus propios artículos, aunque no tenga un permiso global explícito.

El desafío de usar @PreAuthorize(hasPermission(…)) con distintos tipos de parámetros (cuando solo tengo el id, no el objeto completo)

Pensar en expansión futura: que el mecanismo de autorización sirva para otras entidades igual que para Articulo.

La importancia de la trazabilidad y logs al tomar decisiones de seguridad, porque ayuda mucho a depurar por qué algo fue permitido o denegado.
