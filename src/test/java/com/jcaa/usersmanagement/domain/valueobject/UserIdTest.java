package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidUserIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("UserId")
class UserIdTest {

  @ParameterizedTest
  @ValueSource(strings = {" user123 ", "  user123  ", "user123\t"})
  @DisplayName("Normaliza el identificador removiendo espacios")
  void shouldCreateUserIdWithTrimmedValue(String input) {
    final String correctUserId = "user123";
    final UserId userId = new UserId(input);
    assertEquals(correctUserId, userId.toString());
  }

  @Test
  @DisplayName("Lanza NullPointerException cuando userId es null")
  void shouldThrowNullPointerExceptionWhenUserIdIsNull() {
    assertThrows(NullPointerException.class, () -> new UserId(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b"})
  @DisplayName("Lanza InvalidUserIdException cuando userId es vacío")
  void shouldThrowIllegalArgumentExceptionWhenUserIdIsEmpty(String input) {
    assertThrows(InvalidUserIdException.class, () -> new UserId(input));
  }
}
