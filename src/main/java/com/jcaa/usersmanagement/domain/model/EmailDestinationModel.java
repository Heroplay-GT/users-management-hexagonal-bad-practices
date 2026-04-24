package com.jcaa.usersmanagement.domain.model;

import lombok.Value;
import java.util.Objects;

@Value
public class EmailDestinationModel {

  private static final String MESSAGE_EMAIL_REQUIRED = "El email del destinatario es requerido.";
  private static final String MESSAGE_NAME_REQUIRED = "El nombre del destinatario es requerido.";
  private static final String MESSAGE_SUBJECT_REQUIRED = "El asunto es requerido.";
  private static final String MESSAGE_BODY_REQUIRED = "El cuerpo del mensaje es requerido.";

  String destinationEmail;
  String destinationName;
  String subject;
  String body;

  public EmailDestinationModel(
      final String destinationEmail,
      final String destinationName,
      final String subject,
      final String body) {
    this.destinationEmail = validateNotBlank(destinationEmail, MESSAGE_EMAIL_REQUIRED);
    this.destinationName  = validateNotBlank(destinationName,  MESSAGE_NAME_REQUIRED);
    this.subject          = validateNotBlank(subject,          MESSAGE_SUBJECT_REQUIRED);
    this.body             = validateNotBlank(body,             MESSAGE_BODY_REQUIRED);
  }

  private static String validateNotBlank(final String value, final String errorMessage) {
    // VIOLACIÓN Regla 4: se usa == null en lugar de Objects.requireNonNull() o Objects.isNull().
    // Para objetos siempre debe usarse Objects.isNull/nonNull, nunca operadores == o !=.
    if (Objects.isNull(value)) {
      throw new NullPointerException(errorMessage);
    }
    // VIOLACIÓN Regla 10: mensajes de error hardcodeados en el cuerpo del método,
    // en lugar de definirse como constantes con nombre descriptivo.
    if (value.trim().isEmpty()) {
      throw new IllegalArgumentException(errorMessage);
    }
    return value;
  }
}
