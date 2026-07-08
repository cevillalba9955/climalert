package ar.edu.utn.frba.ddsi.climaalert.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherResponseDto{
  private long lastUpdateEpoch;
  private String fechaHora;
  private double temperatura;
  private double humedad;
  private String condicion;
  private double velocidadViento;
  private double direccionViento;
  private double precipitacion;
  private double nubosidad;
  private double sensacionTermica;
  private double puntoRocio;
  private double visibilidad;
  private double radiacionUV;
  private double lluvia;
  private double probabilidadLluvia;
  private double nieve;
  private double probabilidadNieve;
}


