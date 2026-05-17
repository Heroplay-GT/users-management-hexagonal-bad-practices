package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidUserNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("UserName")
class UserNameTest {

  @ParameterizedTest
  @ValueSource(strings = {"John Arrieta", "   John Arrieta   ", "John Arrieta \t"})
  @DisplayName("Normaliza y valida un nombre con longitud mínima")
  void shouldValidateUserNameMinimumLength(final String userName) {
    final String correctUserName = "John Arrieta";
    final UserName userNameVo = new UserName(userName);
    assertEquals(correctUserName, userNameVo.toString());
  }

  // -- Flujo con excepciones y ramas de validación ---

  @Test
  @DisplayName("Lanza NullPointerException cuando el nombre es null")
  void shouldValidateUserNameIsNotNull() {
    assertThrows(NullPointerException.class, () -> new UserName(null));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"", "  ", "\t", "\n", "\r", "\f", "\b", "Jo", "Ty  ", "", "   Cy ", "Ed\t"})
  @DisplayName("Lanza InvalidUserNameException cuando el nombre es vacío o muy corto")
  void shouldValidateUserNameIsNotEmptyAndMinimumLength(final String userName) {
    assertThrows(InvalidUserNameException.class, () -> new UserName(userName));
  }
}
