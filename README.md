
# ✅ Spring Error Handler - Guía de Uso

Spring Error Handler es una librería para manejo de errores centralizados en APIs REST usando Spring Boot 3 y Java 21.

---

## 🚀 Alternativas de Uso

### 🔁 Opción 1: Clonar y compilar localmente

```bash
git clone https://github.com/tuusuario/spring-error-handler.git
cd spring-error-handler
mvn clean install
```

Luego, en tu proyecto:

```xml
<dependency>
  <groupId>com.error</groupId>
  <artifactId>spring-error-handler</artifactId>
  <version>1.0.0</version>
</dependency>
```

---

### 📦 Opción 2: Instalar directamente el JAR (si lo tienes)

```bash
mvn install:install-file   -Dfile=path/to/spring-error-handler.jar   -DgroupId=com.error   -DartifactId=spring-error-handler   -Dversion=1.0.0   -Dpackaging=jar
```

---

## 🛠️ Agrega tus mensajes

Archivo: `src/main/resources/i18n/messages.properties`

```properties
default=Ha ocurrido un error inesperado
default.short=ERROR

user.not.found=Usuario no encontrado
user.not.found.short=NOT_FOUND
```

---

## 🚨 Uso en Código

### Error de negocio

```java
throw new BusinessException("user.not.found");
```

### Error personalizado

```java
throw new BusinessException("user.not.found", "Usuario inválido", "NOT_FOUND");
```

### Respuesta controlada

```java
throw new CustomHttpResponseException(new ResponseEntity<>(payload, HttpStatus.OK));
```

---

## 📤 Formato de Respuesta

```json
{
  "errorCode": "user.not.found",
  "message": "Usuario no encontrado",
  "shortMessage": "NOT_FOUND",
  "data": null
}
```

---

## ✅ Requisitos

- Java 21
- Spring Boot 3.x
