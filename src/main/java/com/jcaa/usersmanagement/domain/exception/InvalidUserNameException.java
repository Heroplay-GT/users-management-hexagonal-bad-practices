package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserNameException extends DomainException {

  private static final String MESSAGE_EMPTY = "The user name must not be empty.";
  private static final String MESSAGE_TOO_SHORT = "The user name must have at least %d characters.";

  private InvalidUserNameException(final String message) {
    super(message);
  }

  public static InvalidUserNameException becauseValueIsEmpty() {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
    return new InvalidUserNameException(MESSAGE_EMPTY);
  }

  public static InvalidUserNameException becauseLengthIsTooShort(final int minimumLength) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
    return new InvalidUserNameException(String.format(MESSAGE_TOO_SHORT, minimumLength));
  }
}
