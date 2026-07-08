package ar.edu.utn.frba.ddsi.climaalert.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CondicionHumedadAlta implements ICondicion {
  private double HumedadMaxima = 60;

  public boolean esNormal(Clima clima) {
    return (clima.getHumedad() < HumedadMaxima);
  }
}
