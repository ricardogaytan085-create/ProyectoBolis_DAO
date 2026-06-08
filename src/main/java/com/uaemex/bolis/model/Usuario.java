package com.uaemex.bolis.model;

public record Usuario(int id, String login, String password, String nombre, String rol) {
    public Usuario {
        if (id < 0 || blank(login) || blank(password) || blank(nombre) || blank(rol)) {
            throw new IllegalArgumentException("Usuario invalido");
        }
        if (!"Administrador".equalsIgnoreCase(rol) && !"Empleado".equalsIgnoreCase(rol)) {
            throw new IllegalArgumentException("Rol invalido");
        }
    }

    private static boolean blank(String s) {
        return s == null || s.isBlank();
    }
}
