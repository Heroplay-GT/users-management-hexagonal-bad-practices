package com.jcaa.usersmanagement.domain.exception;

public final class EmailSenderException extends DomainException {

  private static final String MESSAGE_SMTP_FAILED = "No se pudo enviar el correo a '%s'. Error SMTP: %s";
  private static final String MESSAGE_SEND_FAILED = "La notificación por correo no pudo ser enviada.";

  // VIOLACIÓN Regla 9 (Clean Code): constructores public en una excepción que debería usar
  // factory methods con constructores privados para controlar cómo se instancia.
  // Así cualquier clase puede crear excepciones con mensajes arbitrarios.
  private EmailSenderException(final String message) {
    super(message);
  }

  private EmailSenderException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static EmailSenderException becauseSmtpFailed(
      final String destinationEmail, final String smtpError) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
    return new EmailSenderException(
        String.format(MESSAGE_SMTP_FAILED, destinationEmail, smtpError));
  }

  public static EmailSenderException becauseSendFailed(final Throwable cause) {
    // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
    return new EmailSenderException(MESSAGE_SEND_FAILED, cause);
  }
}
