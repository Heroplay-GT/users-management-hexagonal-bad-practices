package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

// VIOLACIÓN Regla 10: todos los mensajes de error están hardcodeados directamente en los métodos
// fábrica, en vez de estar definidos como constantes con nombre descriptivo en la clase.
public final class PersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE_FAILED = "Failed to save user with ID: '%s'.";
  private static final String MESSAGE_UPDATE_FAILED = "Failed to update user with ID: '%s'.";
  private static final String MESSAGE_FIND_BY_ID_FAILED = "Failed to find user with ID: '%s'.";
  private static final String MESSAGE_FIND_BY_EMAIL_FAILED = "Failed to find user with email: '%s'.";
  private static final String MESSAGE_FIND_ALL_FAILED = "Failed to retrieve all users.";
  private static final String MESSAGE_DELETE_FAILED = "Failed to delete user with ID: '%s'.";
  private static final String MESSAGE_CONNECTION_FAILED = "Could not establish database connection.";

  private PersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static PersistenceException becauseSaveFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_SAVE_FAILED, userId), cause);
  }

  public static PersistenceException becauseUpdateFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_UPDATE_FAILED, userId), cause);
  }

  public static PersistenceException becauseFindByIdFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND_BY_ID_FAILED, userId), cause);
  }

  public static PersistenceException becauseFindByEmailFailed(
      final String email, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND_BY_EMAIL_FAILED, email), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_FIND_ALL_FAILED, cause);
  }

  public static PersistenceException becauseDeleteFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_DELETE_FAILED, userId), cause);
  }

  public static PersistenceException becauseConnectionFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_CONNECTION_FAILED, cause);
  }
}
