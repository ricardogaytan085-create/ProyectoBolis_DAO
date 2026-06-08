package com.uaemex.bolis.model;

import java.time.LocalDate;

public record Usuario(int id, String login, String password, String nombres, String apellidoPaterno,
                      String apellidoMaterno, LocalDate fechaNacimiento, String email, String telefono, String rol) {
    public Usuario {
        if (id < 0 || blank(login) || blank(password) || blank(nombres) || blank(apellidoPaterno)
                || blank(apellidoMaterno) || fechaNacimiento == null || blank(email) || blank(telefono) || blank(rol)) {
            throw new IllegalArgumentException("Usuario invalido");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) throw new IllegalArgumentException("Fecha invalida");
        if (!email.contains("@")) throw new IllegalArgumentException("Email invalido");
        if (!"Administrador".equalsIgnoreCase(rol) && !"Empleado".equalsIgnoreCase(rol)) {
            throw new IllegalArgumentException("Rol invalido");
        }
    }

    public String nombre() {
        return (nombres + " " + apellidoPaterno + " " + apellidoMaterno).trim();
    }

    private static boolean blank(String s) {
        return s == null || s.isBlank();
    }
}
