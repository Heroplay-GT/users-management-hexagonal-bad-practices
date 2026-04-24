# Informe de Correcciones - Reglas 1

## Violación 4: Uso de anotación @UtilityClass en clases utilitarias

### Descripción del Problema
La clase `ValidatorProvider` contiene solo métodos estáticos pero:
- No tiene la anotación `@UtilityClass` de Lombok
- No tiene un constructor privado explícito
- Puede ser instanciada accidentalmente, violando el patrón de utilidad

### Solución Implementada
**Archivo**: `src/main/java/com/jcaa/usersmanagement/infrastructure/config/ValidatorProvider.java`

Se agregó la anotación `@UtilityClass`:
```java
@UtilityClass
public final class ValidatorProvider {
    public static Validator buildValidator() {
        // ...
    }
}
```

**Beneficio**: Lombok genera automáticamente un constructor privado que previene instanciación accidental.

---

## Violación 5: Método privado sin estado debe ser static

### Descripción del Problema
El método `renderTemplate()` en `EmailNotificationService` no utiliza estado de la instancia (no usa `this` ni campos), pero estaba declarado como un método de instancia en lugar de `static`.

### Solución Implementada
**Archivo**: `src/main/java/com/jcaa/usersmanagement/application/service/EmailNotificationService.java`

Se cambió el método a `static`:
```java
private static String renderTemplate(String template, final Map<String, String> values) {
    String result = template;
    for (final Map.Entry<String, String> tokenEntry : values.entrySet()) {
        final String token = "{{" + tokenEntry.getKey() + "}}";
        result = result.replace(token, tokenEntry.getValue());
    }
    return result;
}
```

**Beneficio**: Hace explícito que el método no depende de estado de instancia, mejorando la claridad del código.

---

## Violación 6: No retornar null en colecciones

### Descripción del Problema
El método `execute()` en `GetAllUsersService` retornaba `null` cuando la lista de usuarios estaba vacía:
```java
if (users.isEmpty()) {
    return null;  // ❌ VIOLACIÓN
}
```

Esto causa problemas:
- NPE (NullPointerException) en llamadores
- Ambigüedad: ¿null significa error o lista vacía?
- Viola el contrato esperado

### Solución Implementada
**Archivo**: `src/main/java/com/jcaa/usersmanagement/application/service/GetAllUsersService.java`

Se retorna `Collections.emptyList()`:
```java
import java.util.Collections;

@Override
public List<UserModel> execute() {
    final List<UserModel> users = getAllUsersPort.getAll();
    if (users.isEmpty()) {
        return Collections.emptyList();  // ✅ CORRECTO
    }
    return users;
}
```

**Beneficio**: Contrato claro, sin NPE, sin ambigüedad.

---

## Violación 7: Hardcoding de textos de error

### Descripción del Problema
Los textos de error estaban hardcodeados directamente en el código:

**En UserNotFoundException**:
```java
public static UserNotFoundException becauseIdWasNotFound(final String userId) {
    return new UserNotFoundException(
        String.format("The user with id '%s' was not found.", userId)  // ❌ Hardcodeado
    );
}
```

**En EmailDestinationModel**:
```java
this.destinationEmail = validateNotBlank(destinationEmail, "El email del destinatario es requerido.");
this.destinationName  = validateNotBlank(destinationName,  "El nombre del destinatario es requerido.");
// ... más textos hardcodeados
```

### Solución Implementada

**UserNotFoundException**:
```java
public final class UserNotFoundException extends DomainException {
    private static final String MESSAGE_USER_NOT_FOUND = "The user with id '%s' was not found.";
    
    public static UserNotFoundException becauseIdWasNotFound(final String userId) {
        return new UserNotFoundException(String.format(MESSAGE_USER_NOT_FOUND, userId));
    }
}
```

**EmailDestinationModel**:
```java
public class EmailDestinationModel {
    private static final String MESSAGE_EMAIL_REQUIRED = "El email del destinatario es requerido.";
    private static final String MESSAGE_NAME_REQUIRED = "El nombre del destinatario es requerido.";
    private static final String MESSAGE_SUBJECT_REQUIRED = "El asunto es requerido.";
    private static final String MESSAGE_BODY_REQUIRED = "El cuerpo del mensaje es requerido.";
    
    public EmailDestinationModel(...) {
        this.destinationEmail = validateNotBlank(destinationEmail, MESSAGE_EMAIL_REQUIRED);
        this.destinationName  = validateNotBlank(destinationName, MESSAGE_NAME_REQUIRED);
        this.subject          = validateNotBlank(subject, MESSAGE_SUBJECT_REQUIRED);
        this.body             = validateNotBlank(body, MESSAGE_BODY_REQUIRED);
    }
}
```

**Beneficio**: 
- Fácil mantenimiento y cambios de mensajes
- Reutilización consistente
- Cumplimiento de Clean Code

---

## Violación 8: Uso de == null en lugar de Objects.isNull()

### Descripción del Problema
En `EmailDestinationModel`, la validación usaba operador `==` en lugar de `Objects.isNull()`:
```java
if (value == null) {  // ❌ No recomendado
    throw new NullPointerException(errorMessage);
}
```

### Solución Implementada
```java
import java.util.Objects;

private static String validateNotBlank(final String value, final String errorMessage) {
    if (Objects.isNull(value)) {  // ✅ Recomendado
        throw new NullPointerException(errorMessage);
    }
    // ...
}
```

**Beneficio**: Patrón consistente con las mejores prácticas, mejor legibilidad.

---

## Compilación y Pruebas

✅ **Compilación**: `mvn clean compile` - Exitosa

```
[INFO] BUILD SUCCESS
```

## Resumen de Cambios

| Violación | Archivo | Cambio |
|-----------|---------|--------|
| 4 | ValidatorProvider.java | Agregar @UtilityClass |
| 5 | EmailNotificationService.java | Hacer renderTemplate() static |
| 6 | GetAllUsersService.java | Retornar emptyList() en lugar de null |
| 7 | UserNotFoundException.java | Extraer constantes de error |
| 7 | EmailDestinationModel.java | Extraer constantes de error |
| 8 | EmailDestinationModel.java | Usar Objects.isNull() en lugar de == |

---

## Rama y Commit

**Rama**: `fix/regla1/violacion4`

**Commit**: 
```
7e5d2c8 refactor: agregar @UtilityClass a ValidatorProvider - violacion 4
```

**URL del Repositorio**: https://github.com/Heroplay-GT/users-management-hexagonal-bad-practices

---

Fecha: 23 de Abril de 2026
