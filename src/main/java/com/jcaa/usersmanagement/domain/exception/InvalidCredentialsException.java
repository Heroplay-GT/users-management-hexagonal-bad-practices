package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCredentialsException extends DomainException {

  private static final String MESSAGE_INVALID_CREDENTIALS = "Correo o contraseña incorrectos.";

  private InvalidCredentialsException(final String message) {
    super(message);
  }

  public static InvalidCredentialsException becauseCredentialsAreInvalid() {
    // VIOLACIÓN Regla 10: texto de error hardcodeado directamente.
    // Debe usarse una constante con nombre descriptivo en lugar de un String literal.
    return new InvalidCredentialsException(MESSAGE_INVALID_CREDENTIALS);
  }

  public static InvalidCredentialsException becauseUserIsNotActive() {
    // VIOLACIÓN Regla 10: texto de error hardcodeado directamente.
    return new InvalidCredentialsException("Tu cuenta no está activa. Contacta al administrador.");
  }
}
