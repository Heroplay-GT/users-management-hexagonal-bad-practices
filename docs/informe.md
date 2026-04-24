# Informe de Correcciones - Regla 1: Arquitectura Hexagonal

## Resumen

Se han implementado 7 violaciones de la Regla 1 identificadas en el código base. Cada violación fue corregida en una rama independiente siguiendo el protocolo de git establecido (feature branches, merge a main).

---

## Violación 4: Uso de anotación @UtilityClass en clases utilitarias

### Descripción del Problema
La clase `ValidatorProvider` contiene solo métodos estáticos pero:
- No tiene la anotación `@UtilityClass` de Lombok
- No tiene un constructor privado explícito
- Puede ser instanciada accidentalmente, violando el patrón de utilidad

### Solución Implementada
**Archivo**: `ValidatorProvider.java`

Se agregó la anotación `@UtilityClass`:
```java
@UtilityClass
public final class ValidatorProvider {
    public static Validator buildValidator() { ... }
}
```

---

## Violación 5: Magic numbers y Objects.isNull()

### Descripción del Problema
Múltiples archivos tenían:
- **Magic numbers**: constantes hardcodeadas (8, 12, 3) sin nombres descriptivos
- **Comparación == null**: Se usaba `==` en lugar de `Objects.isNull()` o `Objects.requireNonNull()`

### Archivos Corregidos

#### UserPassword.java
- Agregadas constantes: `MINIMUM_LENGTH = 8`, `BCRYPT_COST = 12`
- Cambio: `if (plainText == null)` → `if (Objects.isNull(plainText))`
- Cambio: `BCrypt.withDefaults().hashToString(12, ...)` → `hashToString(BCRYPT_COST, ...)`
- Cambio: `if (normalizedValue.length() < 8)` → `if (normalizedValue.length() < MINIMUM_LENGTH)`

#### UserName.java
- Agregada constante: `MINIMUM_LENGTH = 3`
- Cambio: `if (value == null)` → `if (Objects.isNull(value))`
- Cambio: `if (normalizedValue.length() < 3)` → `if (normalizedValue.length() < MINIMUM_LENGTH)`

#### UserId.java
- Cambio: `if (value == null)` → `if (Objects.isNull(value))`

#### InvalidUserNameException.java
- Extraídas constantes:
  - `MESSAGE_EMPTY = "The user name must not be empty."`
  - `MESSAGE_TOO_SHORT = "The user name must have at least %d characters."`

---

## Violación 6: Logging y manejo de excepciones en aplicación

### Descripción del Problema

#### DeleteUserService.java
- Tenía un `Logger` manual creado con `Logger.getLogger()`
- Try-catch innecesario que solo loguea y re-lanza sin recuperación
- Pattern: `try { ... } catch (Exception e) { logger.warning(...); throw e; }`

#### UserEmail.java
- Logging en capa de dominio (el dominio **no debe** tener logs)
- Logging de PII (Personally Identifiable Information): el email del usuario
- `LOGGER.warning("Validando email del usuario: " + normalizedValue);`

### Solución Implementada

#### DeleteUserService.java
```java
// ANTES
private static final Logger logger = Logger.getLogger(...);
try {
    validateCommand(command);
    // ... más código
} catch (final Exception e) {
    logger.warning("Error al eliminar usuario: " + e.getMessage());
    throw e;
}

// DESPUÉS
public void execute(final DeleteUserCommand command) {
    validateCommand(command);
    final UserId userId = UserApplicationMapper.fromDeleteCommandToUserId(command);
    ensureUserExists(userId);
    deleteUserPort.delete(userId);
}
```

#### UserEmail.java
```java
// ANTES
private static final Logger LOGGER = Logger.getLogger(UserEmail.class.getName());
LOGGER.warning("Validando email del usuario: " + normalizedValue);

// DESPUÉS
// Se removió completamente el Logger y el log de PII
```

---

## Violación 7: Hardcoding de strings y abreviaturas en nombres

### Descripción del Problema

#### UserManagementCli.java
- **Hardcoded string**: El separador `"===================="` estaba repetido en lugar de usar la constante `MENU_BORDER`
- **Abreviatura**: Variable `opt` en lugar del nombre descriptivo `option`

#### UpdateUserHandler.java
- **Abreviaturas**: Variables `pw` (password) y `upd` (updatedUser)
- Falta de claridad en el código

### Solución Implementada

#### UserManagementCli.java
```java
// ANTES
console.println("  ==========================================");
for (final MenuOption opt : MenuOption.values()) {
    console.printf("    [%d] %s%n", opt.getNumber(), opt.getDescription());
}

// DESPUÉS
console.println(MENU_BORDER);
for (final MenuOption option : MenuOption.values()) {
    console.printf("    [%d] %s%n", option.getNumber(), option.getDescription());
}
```

#### UpdateUserHandler.java
```java
// ANTES
final String pw = console.readOptional("New password (leave blank to keep current): ");
final UserResponse upd = userController.updateUser(
    new UpdateUserRequest(id, name, email, pw.isBlank() ? null : pw, role, status));

// DESPUÉS
final String password = console.readOptional("New password (leave blank to keep current): ");
final UserResponse updatedUser = userController.updateUser(
    new UpdateUserRequest(id, name, email, password.isBlank() ? null : password, role, status));
```

---

## Resumen de Cambios por Violación

| Violación | Archivo(s) | Cambio Principal |
|-----------|-----------|------------------|
| 4 | ValidatorProvider.java | Agregar @UtilityClass |
| 5 | UserPassword.java | Constantes MINIMUM_LENGTH, BCRYPT_COST + Objects.isNull() |
| 5 | UserName.java | Constante MINIMUM_LENGTH + Objects.isNull() |
| 5 | UserId.java | Objects.isNull() |
| 5 | InvalidUserNameException.java | Extraer constantes de mensajes |
| 6 | DeleteUserService.java | Eliminar Logger manual y try-catch innecesario |
| 6 | UserEmail.java | Eliminar logging en dominio y PII |
| 7 | UserManagementCli.java | Usar MENU_BORDER constant y renombrar `opt` → `option` |
| 7 | UpdateUserHandler.java | Renombrar `pw` → `password`, `upd` → `updatedUser` |

---

## Ramas y Commits Git

| Violación | Rama | Commit |
|-----------|------|--------|
| 4 | fix/regla1/violacion4 | 7e5d2c8 |
| 5 | fix/regla1/violacion5 | ea5eb97 |
| 6 | fix/regla1/violacion6 | ff3f12e |
| 7 | fix/regla1/violacion7 | 329cf53 |

---

## Compilación y Pruebas

✅ **Compilación**: `mvn clean compile` - Exitosa en todas las violaciones

```
[INFO] BUILD SUCCESS
```

---

## Principios y Reglas Aplicadas

### Regla 4: Estilos y Naming
- Nombres claros, sin abreviaturas
- Evitar `==` en objetos, usar `Objects.equals()` o `Objects.isNull()`
- No usar imports con `*`
- Métodos sin estado → `static`
- Clases utilitarias → `@UtilityClass`

### Regla 6: Excepciones, Logging y Telemetría
- `try-catch` solo si hay recuperación
- No loguear PII
- Dominio: sin logs
- Entrypoints/adapters: sí logs
- Manejo global de errores

### Regla 10: Calidad del Código
- Sin magic numbers
- Usar constantes con nombres descriptivos
- No hardcodear textos

---

## URL del Repositorio

**Repositorio**: https://github.com/Heroplay-GT/users-management-hexagonal-bad-practices

**Rama main**: Contiene todas las violaciones integradas

---

Fecha: 23 de Abril de 2026

