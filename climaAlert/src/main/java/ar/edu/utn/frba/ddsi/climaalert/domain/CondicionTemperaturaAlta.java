package ar.edu.utn.frba.ddsi.climaalert.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CondicionTemperaturaAlta implements ICondicion {
  double temperaturaMaxima = 35;

  @Override
    public boolean esNormal(Clima clima) {
      return (clima.getTemperatura() < temperaturaMaxima);
  }
}
