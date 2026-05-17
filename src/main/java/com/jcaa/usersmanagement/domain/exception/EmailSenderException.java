package com.jcaa.usersmanagement.domain.exception;

public final class EmailSenderException extends DomainException {

  private static final String MESSAGE_SMTP_FAILED = "No se pudo enviar el correo a '%s'. Error SMTP: %s";
  private static final String MESSAGE_SEND_FAILED = "La notificación por correo no pudo ser enviada.";

  private EmailSenderException(final String message) {
    super(message);
  }

  private EmailSenderException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static EmailSenderException becauseSmtpFailed(
      final String destinationEmail, final String smtpError) {
    return new EmailSenderException(
        String.format(MESSAGE_SMTP_FAILED, destinationEmail, smtpError));
  }

  public static EmailSenderException becauseSendFailed(final Throwable cause) {
    return new EmailSenderException(MESSAGE_SEND_FAILED, cause);
  }
}
