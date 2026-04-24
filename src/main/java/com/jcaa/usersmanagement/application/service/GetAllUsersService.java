package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllUsersUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllUsersPort;
import com.jcaa.usersmanagement.domain.model.UserModel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public final class GetAllUsersService implements GetAllUsersUseCase {

  private final GetAllUsersPort getAllUsersPort;

  @Override
  public List<UserModel> execute() {
    // VIOLACIÓN Regla 5 (Reglas 1.md): ningún método debe retornar null.
    // VIOLACIÓN Regla 21 (Clean Code — no retornar banderas de error):
    // El contrato de GetAllUsersPort.getAll() garantiza una Lista (nunca null).
    // Si no hay usuarios, retorna una lista vacía, no null.
    // Esto diferencia claramente: lista vacía = sin usuarios, null = no debería suceder.
    final List<UserModel> users = getAllUsersPort.getAll();
    return Objects.requireNonNullElseGet(users, List::of);
  }
}
