package com.uaemex.bolis.model;

import java.time.LocalDateTime;

public record Venta(int id, int boliId, int usuarioId, int cantidad, double totalVenta, LocalDateTime fechaHora) {}
