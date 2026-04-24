package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

// DTO de salida inmutable con record.
public record UserResponse(
    String id,
    String name,
    String email,
    String role,
    String status) {}
