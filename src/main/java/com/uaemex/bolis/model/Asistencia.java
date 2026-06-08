package com.uaemex.bolis.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record Asistencia(int id, int usuarioId, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida) {}
