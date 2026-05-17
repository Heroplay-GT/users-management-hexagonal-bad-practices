package com.jcaa.usersmanagement.infrastructure.config;

public final class ConfigurationException extends RuntimeException {

  private static final String MESSAGE_LOAD_FAILED = "Failed to load the application configuration: %s";

  private ConfigurationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static ConfigurationException becauseLoadFailed(final Throwable cause) {
    return new ConfigurationException(
        String.format(MESSAGE_LOAD_FAILED, cause.getMessage()), cause);
  }
}
