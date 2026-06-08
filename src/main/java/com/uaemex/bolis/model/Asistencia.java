package com.uaemex.bolis.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record Asistencia(int id, int usuarioId, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida) {
    public Asistencia {
        if (id < 0 || usuarioId <= 0 || fecha == null) throw new IllegalArgumentException("Asistencia invalida");
        if (horaEntrada != null && horaSalida != null && horaSalida.isBefore(horaEntrada)) {
            throw new IllegalArgumentException("Horario de asistencia invalido");
        }
    }
}
