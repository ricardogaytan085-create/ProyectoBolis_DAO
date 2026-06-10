package com.uaemex.bolis.model;

import java.time.LocalDateTime;
//Editado por Gwendy
public record Venta(int id, int boliId, int usuarioId, int cantidad, double totalVenta, LocalDateTime fechaHora) {
    public Venta {
        if (id < 0 || boliId <= 0 || usuarioId <= 0 || cantidad <= 0 || totalVenta <= 0 || fechaHora == null) {
            throw new IllegalArgumentException("Venta invalida");
        }
    }

    @Override
    public String toString() {
        return id + " | boli " + boliId + " | " + cantidad + " | $" + totalVenta + " | " + fechaHora;
    }
}
