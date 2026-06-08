package com.uaemex.bolis.model;

public record Boli(int id, String sabor, double precio, int stock) {
    public Boli {
        if (id < 0 || sabor == null || sabor.isBlank() || precio <= 0 || stock < 0) {
            throw new IllegalArgumentException("Datos de boli invalidos");
        }
    }
}
